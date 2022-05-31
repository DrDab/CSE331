import { Component } from "react";
import { Edge } from "./GeoConstructs";

interface NavSelectorProps
{
    onEdgesReady(edges: Edge[]) : void;
}

interface NavSelectorState
{
    buildings: string[];
    srcBldg: string,
    destBldg: string
}

class NavSelector extends Component<NavSelectorProps, NavSelectorState> 
{
    constructor(props: any)
    {
        super(props);
        this.state = {buildings: [], srcBldg: "", destBldg:"" };
        this.sendRequest();
    }

    async sendRequest() {
      try
      {
        let responsePromise = fetch("http://localhost:4567/getBuildings");
        let response = await responsePromise;
        let parsingPromise = response.json();
        let parsedObject = await parsingPromise;
        let tbuildings = []

        for (const [key, value] of Object.entries(parsedObject)) 
        {
          tbuildings.push(key);
        }

        this.setState({buildings: tbuildings});
        console.log("sendRequest done!");
      }
      catch (e)
      {
        console.log(e);
      }
      
    }

    onSrcBldgChanged(event: any) 
    {
      console.log("Src building set: "+event.target.value);
      this.setState({srcBldg: event.target.value});
    }

    onDestBldgChanged(event: any) 
    {
      console.log("Dest building set: "+event.target.value);
      this.setState({destBldg: event.target.value});
    }

    onFindPathClicked(event: any) 
    {
      let srcBldg: string = this.state.srcBldg;
      let destBldg: string = this.state.destBldg;
      let buildings: string[] = this.state.buildings;

      let srcBldgNonExistent: boolean = buildings.indexOf(srcBldg) == -1;
      let destBldgNonExistent: boolean = buildings.indexOf(destBldg) == -1;

      if (srcBldgNonExistent || destBldgNonExistent)
      {
        alert(srcBldgNonExistent && destBldgNonExistent ? "\"From\" and \"To\" buildings are invalid!" : 
              srcBldgNonExistent ? "\"From\" building is invalid!" : "\"To\" building is invalid!");
        return;
      }

      this.props.onEdgesReady([]);
    }

    render() 
    {
        return (
            <div>
            <button onClick={e => {this.onFindPathClicked(e)}}>Find Path</button>
            &nbsp;
            from
            &nbsp;
            <select onChange={e => this.onSrcBldgChanged(e)}>
              <option value="Select Src Bldg...">Select Src Bldg</option>
              {
                this.state.buildings.map((option) => (
                    <option value={option}>{option}</option>
                ))
              }
            </select>
            &nbsp;
            to
            &nbsp;
            <select onChange={e => this.onDestBldgChanged(e)}>
              <option value="Select Dest Bldg...">Select Dest Bldg</option>
              {
                this.state.buildings.map((option) => (
                    <option value={option}>{option}</option>
                ))
              }
            </select>
            </div>
          );
      
    }

}

export default NavSelector;
