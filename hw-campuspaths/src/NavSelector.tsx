import { Component } from "react";
import { Edge, Point } from "./GeoConstructs";

interface NavSelectorProps
{
    onEdgesReady(edges: Edge[]) : void;
    onPointsChanged(startPoint: Point|null, endPoint: Point|null) : void
}

interface NavSelectorState
{
    buildings: Map<string, string>;
    srcBldg: string,
    destBldg: string
    ready: boolean
    pathDistance: number
}

class NavSelector extends Component<NavSelectorProps, NavSelectorState> 
{
    constructor(props: any)
    {
        super(props);
        this.state = {buildings: new Map<string, string>(), srcBldg: "", destBldg:"", ready: false, pathDistance: 0.0 };
        this.loadBldgNames();
    }

    render() 
    {
      console.log("NavSelector render called!");
      let bldgDropdown = this.getBldgDropdown();

        return (
            <div>
            <button onClick={e => {this.onFindPathClicked(e)}}>Find Path</button>
            &nbsp;
            from
            &nbsp;
            <select onChange={e => this.onSrcBldgChanged(e)}>

              <option value="Select Src Bldg...">Select Src Bldg</option>
              { 
                bldgDropdown
              }

            </select>
            &nbsp;
            to
            &nbsp;
            <select onChange={e => this.onDestBldgChanged(e)}>

              <option value="Select Dest Bldg...">Select Dest Bldg</option>
              { 
                bldgDropdown
              }

            </select>

            <br/><br/>
            
            <strong>
            {
              this.state.ready ? "Distance from " + this.state.srcBldg + " to " + 
                this.state.destBldg + ": " + this.round(this.state.pathDistance, 1) + " ft (" + 
                this.round(this.state.pathDistance * 12.0 * .0254, 1) + " m)" : ""
            }
            </strong>

            </div>
          );
      
    }

    async loadBldgNames() 
    {
      try
      {
        let responsePromise = fetch("http://localhost:4567/getBuildings");
        let response = await responsePromise;
        let parsingPromise = response.json();
        let parsedObject = await parsingPromise;
        let tbuildings : Map<string, string> = this.state.buildings;

        for (const [key, value] of Object.entries(parsedObject)) 
        {
          tbuildings.set(key, value + "");
        }

        this.setState({buildings: tbuildings});
        console.log("sendRequest found buildings: " + this.state.buildings);
      }
      catch (e)
      {
        console.log(e);
        alert("Failed to load buildings.");
      }
    }

    async loadPath(srcBldg:string, destBldg:string)
    {
      try
      {
        let srcEnc = encodeURIComponent(srcBldg);
        let destEnc = encodeURI(destBldg);
        let responsePromise = fetch(`http://localhost:4567/getPath?src=${srcEnc}&dest=${destEnc}`);
        let response = await responsePromise;
        let parsingPromise = response.json();
        let parsedObject = await parsingPromise;

        let edges:Edge[] = [];
        let firstPoint: Point|null = null;
        let lastPoint: Point|null = null;

        for (let i in parsedObject["path"])
        {
          let pEdge = parsedObject["path"][i];
          let start = pEdge["start"];
          let end = pEdge["end"];
          let edge:Edge = {id: i, color:"blue", x1:start["x"], y1:start["y"], x2:end["x"], y2:end["y"]};

          if (firstPoint == null)
          {
            firstPoint = {x:start["x"], y:start["y"]};
          }

          lastPoint = {x:end["x"], y:end["y"]};

          edges.push(edge);
        }

        this.setState({ready: true, pathDistance: parsedObject["cost"]});
        this.props.onPointsChanged(firstPoint, lastPoint);
        this.props.onEdgesReady(edges);
        console.log("Path loaded!");
        console.log(edges);
      }
      catch (e)
      {
        console.log(e);
        alert("Failed to load path.");
      }
    }

    onSrcBldgChanged(event: any) 
    {
      console.log("Src building set: "+ event.target.value);
      this.setState({srcBldg: event.target.value, ready: false});
    }

    onDestBldgChanged(event: any) 
    {
      console.log("Dest building set: "+ event.target.value);
      this.setState({destBldg: event.target.value , ready: false});
    }

    onFindPathClicked(event: any) 
    {
      console.log("onFindPathClicked called");
      let srcBldg: string = this.state.srcBldg;
      let destBldg: string = this.state.destBldg;
      let buildings: Map<string, string> = this.state.buildings;

      let srcBldgNonExistent: boolean = !buildings.has(srcBldg);
      let destBldgNonExistent: boolean = !buildings.has(destBldg);

      if (srcBldgNonExistent || destBldgNonExistent)
      {
        alert(srcBldgNonExistent && destBldgNonExistent ? "\"From\" and \"To\" buildings are invalid!" : 
              srcBldgNonExistent ? "\"From\" building is invalid!" : "\"To\" building is invalid!");
        return;
      }

      this.loadPath(srcBldg, destBldg);
    }

    getBldgDropdown() : JSX.Element[]
    {
      console.log("getBldgDropdown called");
      let buildings: Map<string, string> = this.state.buildings;
      let res: JSX.Element[] = []

      for (let key of buildings.keys())
      {
        res.push(<option value={key}>{buildings.get(key)}</option>);
      }

      return res;
    }

    round(value:number, precision:number) : number
    {
      var multiplier = Math.pow(10.0, precision);
      return Math.round(value * multiplier) / multiplier;
    }
}

export default NavSelector;
