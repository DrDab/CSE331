/*
 * Copyright (C) 2022 Kevin Zatloukal.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 331 for use solely during Spring Quarter 2022 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */

import React, {Component} from 'react';
import Map from "./Map";

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";

import { Edge, Point } from './GeoConstructs';
import NavSelector from './NavSelector';

interface AppState
{
  routeEdges: Edge[],        // the Edges of the campus route to render on map (in UW coordinates)
  startPoint: Point|null,    // the source point of the campus route to render on map (in UW coordinates)
  endPoint: Point|null       // the destination point of the campus route to render on map (in UW coordinates)
}

class App extends Component<{}, AppState> 
{
    constructor(props: any)
    {
      super(props);

      this.state = 
      {
         routeEdges: [],
         startPoint: null,
         endPoint: null
      };
    }

    render() 
    {
        return (
            <div>
              <h1 id="app-title" style={{textAlign: 'center'}}>
                Campus Paths
              </h1>

              { /* Map taking in the route's start point, end point and edges in the state of App as props. */ }

              <div>
                <Map  routeEdges={this.state.routeEdges} 
                      startPoint={this.state.startPoint} 
                      endPoint={this.state.endPoint} />
              </div>

              <div style={{textAlign: 'center'}} >

                <br/>

                { /* The "control panel" used to specify the source and destination buildings to pathfind between. */ }

                <NavSelector onEdgesReady={(edges) => 
                  {
                    this.setState({routeEdges: edges});
                  }}

                  onPointsChanged={(startPoint, endPoint) => 
                  {
                    this.setState({startPoint: startPoint, endPoint: endPoint});
                  }}
                />

              </div>
             
            </div>
          );
      
    }

}

export default App;
