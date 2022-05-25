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

import React, { Component } from "react";
import EdgeList, { Edge } from "./EdgeList";
import Map, { Point } from "./Map";

// Allows us to write CSS styles inside App.css, any styles will apply to all components inside <App />
import "./App.css";

interface AppState {
  myEdges:Edge[]
  myPoints:Point[]
}

class App extends Component<{}, AppState> { // <- {} means no props.

  constructor(props: any) {
    super(props);
    this.state = {
       myEdges: [],
       myPoints: []
    };
  }

  render() {
    return (
      <div>
        <h1 id="app-title">Line Mapper!</h1>
        <div>
          <Map myEdges={this.state.myEdges} 
               myPoints={this.state.myPoints}

               onAddPointClicked = {(point) => {
                 let points = this.state.myPoints;
                 points.push(point);
                 console.log("Added point (" + point.x + "," + point.y + ") to points");
                 this.setState({myPoints : points});
                }}

                onUndoPointClicked = {() => {
                  let points = this.state.myPoints;
                  if (points.length > 0)
                  {
                    let removed:Point|undefined = points.pop();
                    if (removed !== undefined)
                    {
                      console.log("Removed point (" + removed.x + "," + removed.y + ") from points");
                      this.setState({myPoints : points});
                    }
                  }
                }}
                
                onClearAllPointsClicked = {() => {
                  this.setState({myPoints : []});
                }}

                onMeasureDistClicked = {() => {

                }}
               />
        </div>
        <EdgeList
          onChange={(edges) => {
            this.setState({myEdges: edges});
          }}
        />
      </div>
    );
  }
}

export default App;
