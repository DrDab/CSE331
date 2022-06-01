import { Component } from "react";
import { Edge, Point } from "./GeoConstructs";

interface NavSelectorProps
{
    onEdgesReady(edges: Edge[]) : void;                                   // callback when the edges for a valid route are found.
    onPointsChanged(startPoint: Point|null, endPoint: Point|null) : void  // callback when the start/end points for a route are found.
}

interface NavSelectorState
{
    buildings: Map<string, string>; // maps the shorthand names of UW buildings to their respective full names.
    srcBldg: string,                // the shorthand name of the selected source building to pathfind from.
    destBldg: string                // the shorthand name of the selected destination building to pathfind to.
    pathSuccessful: boolean         // whether the pathfinding was successful.
    pathDistance: number            // the distance of the computed path between the source building and destination building, in feet.
}

// the host name of the JSON-API HTTP server to do pathfinding from.
const API_SERVER_HOSTNAME = "http://localhost:4567";

// the placeholder name of the default building option in the 
// from/to building selection dropdown lists.
const BLDG_PLACEHOLDER_NAME = "$$$$!OPT_SELECT_BLDG!$$$$";

class NavSelector extends Component<NavSelectorProps, NavSelectorState> 
{
    constructor(props: any)
    {
        super(props);
        this.state = {  buildings: new Map<string, string>(), srcBldg: "", destBldg:"",
                        pathSuccessful: false, pathDistance: 0.0 };

        // asynchronously load the building names from API server.
        this.loadBldgNames();
    }

    render() 
    {
      console.log("NavSelector render called!");

      // generate the dropdown list of buildings from the building map in the state.
      let bldgDropdown = this.getBldgDropdown();

        return (
            <div>
            <button onClick={e => {this.onFindPathClicked(e)}}>Find Path</button>
            &nbsp;
            from
            &nbsp;
            <select onChange={e => this.onSrcBldgChanged(e)} value={this.state.srcBldg}>
              {
                <option value={ BLDG_PLACEHOLDER_NAME }>Select Src Bldg</option>
              }

              { 
                bldgDropdown
              }
            </select>
            &nbsp;
            to
            &nbsp;
            <select onChange={e => this.onDestBldgChanged(e)} value={this.state.destBldg}>
              {
                <option value={ BLDG_PLACEHOLDER_NAME }>Select Dest Bldg</option>
              }

              {
                bldgDropdown
              }
            </select>

            <br/><br/>
            
            <strong>
            {
              this.state.pathSuccessful ? "Distance from " + this.state.srcBldg + " to " + 
                this.state.destBldg + ": " + this.round(this.state.pathDistance, 1) + " ft (" + 
                this.round(this.state.pathDistance * 12.0 * .0254, 1) + " m)" : ""
            }
            </strong>

            <br/><br/>
            <button onClick={e => this.reset()}>Reset</button>
            </div>
          );
      
    }

    // Resets the content displayed on screen to its original.
    reset()
    {
      this.props.onEdgesReady([]);
      this.props.onPointsChanged(null, null);
      this.setState({ pathSuccessful: false, 
                      srcBldg: BLDG_PLACEHOLDER_NAME, 
                      destBldg: BLDG_PLACEHOLDER_NAME });
    }

    // Contacts the API server w/ page /getBuildings and sets this.state.buildings to the value
    // of the JSON data returned mapping each building's shorthand name to its full name.
    // If unsuccessful, creates alert dialogue with message "Failed to load buildings."
    async loadBldgNames() 
    {
      try
      {
        let responsePromise = fetch(API_SERVER_HOSTNAME + "/getBuildings");
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

    // contacts the API server to get the path from buildings with shorthand names,
    // srcBldg (the source building) and destBldg (the destination building), and triggers
    // the callbacks onPointsChanged and onEdgesReady when a path is found. If not successful,
    // creates alert dialogue with message "Failed to load path."
    async loadPath(srcBldg:string, destBldg:string)
    {
      try
      {
        let srcEnc = encodeURIComponent(srcBldg);
        let destEnc = encodeURI(destBldg);
        let responsePromise = fetch(API_SERVER_HOSTNAME + `/getPath?src=${srcEnc}&dest=${destEnc}`);
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

        this.setState({pathSuccessful: true, pathDistance: parsedObject["cost"]});
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

    // Updates the state's source building to the shorthand name of the selected
    // source building, when the selected source building in the dropdown box is changed.
    onSrcBldgChanged(event: any) 
    {
      console.log("Src building set: "+ event.target.value);
      this.setState({srcBldg: event.target.value, pathSuccessful: false});
    }

    // Updates the state's destination building to the shorthand name of the selected
    // destination building, when the selected source building in the dropdown box is changed.
    onDestBldgChanged(event: any) 
    {
      console.log("Dest building set: "+ event.target.value);
      this.setState({destBldg: event.target.value , pathSuccessful: false});
    }

    // Checks if the selected buildings exist on the campus map before calling loadPath for
    // the selected source/destination buildings. If the buildings don't exist, then
    // creates an alert message stating the source/destination buildings don't exist instead of
    // loading the path.
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

    // generates the contents of the building dropdown list from the buildings map
    // in the state.
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

    // rounds a given decimal number value to a given precision, precision.
    round(value:number, precision:number) : number
    {
      var multiplier = Math.pow(10.0, precision);
      return Math.round(value * multiplier) / multiplier;
    }
}

export default NavSelector;
