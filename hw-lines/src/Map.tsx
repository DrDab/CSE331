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
import { Edge, Point } from "./GeoConstructs";
import ReactLeafletKml from 'react-leaflet-kml'; // react-leaflet-kml must be loaded AFTER react-leaflet

// This defines the location of the map. These are the coordinates of the UW Seattle campus
const position: LatLngExpression = [UW_LATITUDE_CENTER, UW_LONGITUDE_CENTER];

// constant values for distance measurement tool states
const DT_IDLE = 0;
const DT_AWAIT_SRC_POINT = 1;
const DT_AWAIT_DEST_POINT = 2;
const DT_FINISHED = 3;

interface MapProps {
  myEdges: Edge[]
}

interface MapState {
  myPoints: Point[],    // the list of user-added 2D Points, in Line Mapper coordinates.
  pointAddX: string,    // string contents of X-coordinate textbox of point addition form.
  pointAddY: string,    // string contents of Y-coordinate textbox of point addition form.
  distState: number,    // the "state" of the distance measurement tool's state machine. can be DT_IDLE, DT_AWAIT_SRC_POINT, DT_AWAIT_DEST_POINT, DT_FINISHED.
  distP1Lat: number,    // the latitude of the selected source point when measuring distance from a source point.
  distP1Lng: number,    // the longitude of the selected source point when measuring distance from a source point.
  distP2Lat: number,    // the latitude of the selected dest point when measuring distance from a source point.
  distP2Lng: number,    // the longitude of the selected dest point when measuring distance from a source point.
  distMsg: string,      // the message to show the user in the distance measurement tool prompt.
  cursor: string        // the CSS name of the cursor to be used within the map.
  showToilets: boolean  // whether to show the toilets on campus
  toiletsLoaded:boolean // whether the toilets KML file has been loaded yet
  toiletsKMLDoc:Document|null // KML document of toilets on campus
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

// custom icon for start flag w/ offset correction added
const startIcon = L.icon({
  iconUrl: 'start_flag.png',
  iconSize: [32,32],
  iconAnchor: [4.5,31]
});

// custom icon for end flag w/ offset correction added
const finishIcon = L.icon({
  iconUrl: 'finish_flag.png',
  iconSize: [32,32],
  iconAnchor: [3.5,31]
});

// click handler to get latitude/longitude of mouse click on map
const LocationFinderDummy = (props: {onClick(lat:number, lng:number) : void}) => {
  useMapEvents({
      click(e) {
        let latLng = e.latlng;
        props.onClick(latLng.lat, latLng.lng);
      },
  });
  return null;
};

class Map extends Component<MapProps, MapState> 
{
  constructor(props: any)
  {
    super(props);
    this.state = { myPoints: [], pointAddX: "", pointAddY: "", distState: DT_IDLE, distP1Lat: 0, 
                    distP1Lng: 0, distP2Lat: 0, distP2Lng:0, 
                    distMsg: "Click \"Measure Distance\" to start.", cursor: "grab", showToilets:false,
                    toiletsLoaded:false, toiletsKMLDoc:null };
  }

  handleMapClick(lat:number, lng:number)
  {
    console.log("Map coordinates clicked: Lat=" + lat + ", Lng=" + lng);
    let distState = this.state.distState;

    if (distState === DT_AWAIT_SRC_POINT)
    {
      // if waiting for the source point to be clicked, now the source point's
      // coordinates have been received. store them and update state to awaiting
      // the destination point to be clicked.
      this.setState({distP1Lat: lat, distP1Lng: lng, distState: DT_AWAIT_DEST_POINT, 
                     distMsg: "Please click dest point on map."});
    }
    else if (distState === DT_AWAIT_DEST_POINT)
    {
      // the destination point has been clicked! 
      // first, store the destination coordinate's points in the state
      // compute the distance between the two points' coordinates.
      let distance = this.getDistance(this.state.distP1Lat, this.state.distP1Lng, lat, lng);
      // distance is in meters can be converted to feet using formula: feet = meters / [ (.0254 meters / inch) * (12 inch / foot) ]
      let distanceFreedom = distance / (.0254 * 12.0);
      let distanceRounded = this.round(distance, 1);
      let distanceFreedomRounded = this.round(distanceFreedom, 1);
      this.setState({distState: DT_FINISHED, distMsg: "Distance: " + distanceRounded + " m (" + 
                     distanceFreedomRounded + " ft)", cursor: "grab", distP2Lat: lat, distP2Lng: lng});
    }
  }

  render() 
  {
    console.log("Map render called");
    let edges = this.props.myEdges;
    let points = this.state.myPoints;

    fetch('./toilets.kml')
    .then(response => response.text())
    .then(data => {
      this.setState({toiletsKMLDoc: new DOMParser().parseFromString(data, 'text/xml'), 
                    toiletsLoaded: true});
    });
    

    return (
      <div id="map" style={{ cursor: this.state.cursor }}>
        <MapContainer
          center={position}
          zoom={15}
          scrollWheelZoom={false}
          style={{cursor: "inherit"}}
        >

          <TileLayer
            attribution='&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
          />

          <LocationFinderDummy onClick={(lat, lng) => 
          {
            this.handleMapClick(lat, lng);
          }} />

          {
            edges.map((edge) => 
            {
              return <MapLine key={edge.id} color={edge.color} x1={edge.x1} y1={edge.y1} x2={edge.x2} y2={edge.y2}/>
            })
          }

          {
            points.map((point) =>
            {
              return <Marker position={{ lat: UW_LATITUDE + (point.y - UW_LATITUDE_OFFSET) * UW_LATITUDE_SCALE, lng:
                UW_LONGITUDE + (point.x - UW_LONGITUDE_OFFSET) * UW_LONGITUDE_SCALE}}
                icon={pointIcon}
              />;
            })
          }

          {
            this.state.distState === DT_IDLE || this.state.distState === DT_AWAIT_SRC_POINT ? [] : 
              this.state.distState === DT_AWAIT_DEST_POINT ?
              <Marker position={{ lat: this.state.distP1Lat, lng:
                this.state.distP1Lng}} icon={startIcon} /> :
                [<Marker position={{ lat: this.state.distP1Lat, lng:
                  this.state.distP1Lng}} icon={startIcon} />,
                  <Marker position={{ lat: this.state.distP2Lat, lng:
                    this.state.distP2Lng}} icon={finishIcon} />]
          }

          {
            this.state.showToilets && this.state.toiletsLoaded && this.state.toiletsKMLDoc != null ? 
            <ReactLeafletKml kml={this.state.toiletsKMLDoc} /> : []
          }

        </MapContainer>

        { /* Other miscellaneous UI elements for adding points/measuring distance/saving image of map follow. */ }

        <button onClick={() => 
            {
              console.log("Save clicked");
              window.print();
            }}>
          Save Image
        </button>
        
        &nbsp;
        &nbsp;
        <div style={{fontFamily: "monospace", display: "inline-block", fontSize:20}}>
        [
        </div>
        &nbsp;

        <div style={{fontFamily: "monospace", display: "inline-block", fontSize:20}}>
        Point X:
        </div>
        
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
        <div style={{fontFamily: "monospace", display: "inline-block", fontSize:20}}>
        Point Y:
        </div>
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
        <div style={{fontFamily: "monospace", display: "inline-block", fontSize:20}}>
        ]
        </div>
        <br/>

        <button 
        onClick={() => 
            {
              // when "Measure Distance" is clicked, set state of distance measurement tool to await source point being clicked.
              // also set the cursor to a crosshair for precise targeting (important when specifying point for distance measurement.)
              this.setState({ distMsg: "Please click source point on map.", distState: DT_AWAIT_SRC_POINT, cursor:"crosshair" });
            }}>
          Measure Distance
        </button>

        &nbsp;
        <div style={{fontFamily: "monospace", display: "inline-block", fontSize:14}}>
        {this.state.distMsg}
        </div>
        &nbsp;

        <br/>
        <button 
        onClick={() => 
            {
              // Locate gender neutral toilets on campus
              this.setState({showToilets:!this.state.showToilets});
            }}>
              
            {this.state.showToilets ? "Hide gender neutral toilets" : 
              "Show gender neutral toilets"}
        </button>
      </div>
    );
  }

  // clears all points in the state (if any) and plays a sound effect if any points were removed.
  clearAllPoints()
  {
    let points = this.state.myPoints;
    if (points.length > 0)
    {
      this.setState({myPoints : []});
      pipe.play();
    }
  }

  // removes the most recently added point in the state, if any, and plays a sound effect if a point was removed.
  // otherwise, ignores call.
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

  // adds a point with the text entered in the point addition form, and plays a sound effect.
  // aborts and creates an alert popup if the values entered are not numbers,
  // or not in range [0,4000].
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

  // returns the radian equivalent of an angle in degrees.
  toRadians(deg : number) : number
  {
    return deg * Math.PI / 180.0;
  }

  // gets the distance in meters between two Earth coordinate pairs (lat, lng) in degrees, from (lat1, lng1) to (lat2, lng2),
  // using the Haversine algorithm.
  getDistance(lat1: number, lng1: number, lat2: number, lng2: number) : number
  {
    let dLat = this.toRadians(lat2 - lat1);
    let dLon = this.toRadians(lng2 - lng1);

    let lat1_rad = this.toRadians(lat1);
    let lat2_rad = this.toRadians(lat2);

    let a = Math.pow(Math.sin(dLat / 2.0), 2) + Math.pow(Math.sin(dLon / 2.0), 2) * Math.cos(lat1_rad) * Math.cos(lat2_rad);
    let rad = 6371000.0; // radius of earth in meters.
    let c = 2 * Math.asin(Math.sqrt(a));

    return rad * c;
  }

  // rounds a number with value 'value' to 'precision' digits of precision, where precision is an integer.
  round(value:number, precision:number) {
    var multiplier = Math.pow(10.0, precision);
    return Math.round(value * multiplier) / multiplier;
  }
}

export default Map;
