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

import L, { LatLngExpression } from "leaflet";
import React, { Component } from "react";
import { MapContainer, Marker, TileLayer, useMapEvents } from "react-leaflet";
import "leaflet/dist/leaflet.css";
import MapLine from "./MapLine";
import { UW_LATITUDE, UW_LATITUDE_CENTER, UW_LATITUDE_OFFSET, UW_LATITUDE_SCALE, UW_LONGITUDE, UW_LONGITUDE_CENTER, UW_LONGITUDE_OFFSET, UW_LONGITUDE_SCALE } from "./Constants";
import { Edge } from "./EdgeList";

// This defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];

interface MapProps {
  myEdges: Edge[]
}

interface MapState {
  myPoints: Point[],
  pointAddX: string,
  pointAddY: string,
  distState: number,
  distLastLat: number,
  distLastLng: number,
  distP1Lat: number,
  distP1Lng: number,
  distP2Lat: number,
  distP2Lng: number
}

export interface Point
{
  x:number,
  y:number
}

// sound effects for point operations
const meow = new Audio("meow.mp3");
const bruh = new Audio("bruh.mp3");
const pipe = new Audio("pipe.mp3");

// custom icon for point w/ offset correction added
const pointIcon = L.icon({
  iconUrl: 'marker_blue.png',
  iconSize: [32,32],
  iconAnchor: [15,32]
});

class Map extends Component<MapProps, MapState> 
{

  constructor(props: any)
  {
    super(props);
    this.state = { myPoints: [], pointAddX: "", pointAddY: "", distState: 0, distP1Lat: 0, 
                    distP1Lng: 0, distP2Lat: 0, distP2Lng: 0, distLastLat: 0, distLastLng: 0 };
  }

  render() 
  {
    console.log("Map render called");
    let edges = this.props.myEdges;
    let points = this.state.myPoints;

    const LocationFinderDummy = (props: {onClick(lat:number, lng:number) : void}) => {
      const map = useMapEvents({
          click(e) {
            let latLng = e.latlng;
            props.onClick(latLng.lat, latLng.lng);
          },
      });
      return null;
    };

    return (
      <div id="map">
        <MapContainer
          center={position}
          zoom={15}
          scrollWheelZoom={false}
        >

          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />

          <LocationFinderDummy onClick={(lat, lng) => {
            console.log("Map coordinates clicked: Lat=" + lat + ", Lng=" + lng);
            this.setState({distLastLat: lat, distLastLng: lng})
          }} />

          {
            edges.map(function (edge) 
            {
              return <MapLine key={edge.id} color={edge.color} x1={edge.x1} y1={edge.y1} x2={edge.x2} y2={edge.y2}/>
            })
          }

          {
            points.map(function (point)
            {
              console.log("(" + point.x + "," + point.y + ")");
              return <Marker position={{ lat: UW_LATITUDE + (point.y - UW_LATITUDE_OFFSET) * UW_LATITUDE_SCALE, lng:
                UW_LONGITUDE + (point.x - UW_LONGITUDE_OFFSET) * UW_LONGITUDE_SCALE}}
                icon={pointIcon}
              />;
            })
          }
        </MapContainer>

        <button onClick={() => 
            {
              console.log("Save clicked");
              window.print();
            }}>
          Save Image
        </button>
        
        &nbsp;
        &nbsp;
        [
        &nbsp;
        Point X:
        <textarea
            rows={1}
            cols={5} 
            style={{resize: "none"}}
            onChange={(e) => {
              this.setState({
                  pointAddX: e.target.value
              })
            }}
            value={this.state.pointAddX}/> 

        &nbsp;
        &nbsp;
        Point Y:
        <textarea
            rows={1}
            cols={5} 
            style={{resize: "none"}}
            onChange={(e) => {
              this.setState({
                pointAddY: e.target.value
              })
            }}
            value={this.state.pointAddY}/> 

        &nbsp;
        &nbsp;

        <button onClick={() => 
            {
              this.addPoint();
            }}>
          Add Point
        </button>

        &nbsp;
        &nbsp;

        <button onClick={() => 
            {
              this.undoLastPoint();
            }}>
          Undo Previous
        </button>

        &nbsp;
        &nbsp;

        <button onClick={() => 
            {
              this.clearAllPoints();
            }}>
          Clear All Points
        </button>

        &nbsp;
        ]
        <br/>
        <button onClick={() => 
            {
              let distState = this.state.distState;
              if (distState == 0)
              {
                this.setState({ distState: 1 });
              }
              else
              {
                this.setState({ distState: 0 });
              }
            }}>
          Measure Distance
        </button>
        &nbsp;
        {this.state.distState}
        &nbsp;
        {this.state.distLastLat + "," + this.state.distLastLng}
      </div>
    );
  }

  clearAllPoints()
  {
    let points = this.state.myPoints;
    if (points.length > 0)
    {
      this.setState({myPoints : []});
      pipe.play();
    }
  }

  undoLastPoint()
  {
    let points = this.state.myPoints;
    if (points.length > 0)
    {
      let removed:Point|undefined = points.pop();
      if (removed !== undefined)
      {
        console.log("Removed point (" + removed.x + "," + removed.y + ") from points");
        this.setState({myPoints : points});
        bruh.play();
      }
    }
  }

  addPoint()
  {
    let xText = this.state.pointAddX;
    let yText = this.state.pointAddY;
    let newX:number = +xText;
    let newY:number = +yText;

    if (isNaN(newX))
    {
      alert("Point X must be a number!");
      return;
    }

    if (isNaN(newY))
    {
      alert("Point Y must be a number!");
      return;
    }

    if (newX < 0 || newX > 4000)
    {
      alert("Point X must be in range [0, 4000]!");
      return;
    }

    if (newY < 0 || newY > 4000)
    {
      alert("Point Y must be in range [0, 4000]!");
      return;
    }

    let points = this.state.myPoints;
    points.push({x:newX, y:newY});
    console.log("Added point (" + newX + "," + newY + ") to points");
    this.setState({myPoints : points});

    meow.play();
  }
}

export default Map;
