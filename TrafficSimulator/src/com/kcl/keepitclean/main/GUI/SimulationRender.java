/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kcl.keepitclean.main.GUI;

import com.kcl.keepitclean.main.policy.Policy;
import com.kcl.keepitclean.main.roadnetwork.junction.Junction;
import com.kcl.keepitclean.main.roadnetwork.junction.TrafficLight;
import com.kcl.keepitclean.main.roadnetwork.road.*;
import com.kcl.keepitclean.main.roadnetwork.laneSection.*;
import com.kcl.keepitclean.main.vehicle.*;
import com.kcl.keepitclean.main.simulatorengine.*;
import com.kcl.keepitclean.main.utils.Constant;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;


import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import static javafx.scene.paint.Color.BLUE;
import static javafx.scene.paint.Color.RED;
import javafx.stage.Stage;

/**
 *
 * @author rosiengo
 */
public class SimulationRender  implements IRenderer {
    private GraphicsContext gc;
    private SimulatorEngine simulation;
    private List<Road> roads;
    private List<Vehicle> vehicles;
    private List<TrafficLight> trafficLights;
    private List<Junction> junctions;
    private Stage stage;
    Constant constant = new Constant();
   
        
   public SimulationRender (GraphicsContext gc, SimulatorEngine simulation)
   {
       this.gc = gc;
       this.simulation = simulation;
       this.stage = stage;       
       
   }
     
    
    public void render() {
      Platform.runLater(new Runnable() {

      @Override
      public void run() {
        clear();
        roads = simulation.getContext().getRoadList();
        vehicles = simulation.getContext().getVehicleList();
        junctions = simulation.getContext().getJunctionList();
        trafficLights = simulation.getContext().getTrafficLightList();
                
        System.out.println("GUI- number of vehicles:" + vehicles.size());
        drawRoads();
        drawTrafficLights();
        drawJunctions();
        drawVehicles();
        
      }
    }
    );

  }

    /*Clear canvas before painting updated components*/
    public void clear() {
        gc.clearRect(0, 0, 800, 600);
    }
    //DRAW ROADS
    public  void drawRoads()
    {
        
        /* draw list of roads generated from simulation engine 
        parameters: List of Roads, that is provided by simulation engine
        */
      
       int j = 0;
       for (Road road: roads)
       {
        j++;
        System.out.println("Road " + j + " start: (" + road.getStartCoordinates().x + "," +  road.getStartCoordinates().y + ")");
        System.out.println("Road " + j + " end: (" + road.getEndCoordinates().x + "," +  road.getEndCoordinates().y + ")");
        
        //draw road
        gc.setFill(Color.DARKGRAY);
               
                
        gc.fillPolygon(new double[] {road.getStartCoordinates().x, road.getEndCoordinates().x, road.getEndCoordinates().x, road.getStartCoordinates().x},
                new double[] {road.getStartCoordinates().y, road.getStartCoordinates().y, road.getEndCoordinates().y, road.getEndCoordinates().y},
                4);
        if (road.getOrientation() == Orientation.HORIZONTAL || 
            road.getOrientation() == Orientation.LEFT_HORIZONTAL ||
            road.getOrientation() == Orientation.RIGHT_HORIZONTAL)
        {
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(1);
            gc.setLineDashes(new double[]{10d,0d});
            gc.strokeLine(road.getStartCoordinates().x, road.getEndCoordinates().y, road.getEndCoordinates().x, road.getEndCoordinates().y);
        
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(1);
            gc.setLineDashes(new double[]{10d,0d});
            gc.strokeLine(road.getStartCoordinates().x, road.getStartCoordinates().y, road.getEndCoordinates().x, road.getStartCoordinates().y);
        }
        
        else 
        {
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(1);
            gc.setLineDashes(new double[]{10d,0d});
            gc.strokeLine(road.getStartCoordinates().x, road.getStartCoordinates().y, road.getStartCoordinates().x, road.getEndCoordinates().y);
        
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(1);
            gc.setLineDashes(new double[]{10d,0d});
            gc.strokeLine(road.getEndCoordinates().x, road.getStartCoordinates().y, road.getEndCoordinates().x, road.getEndCoordinates().y);
 
        }
        
        //draw stroke lane line
        for (int i = 1; i<= road.getNumberOfLanes() -1; i++)
        {
            gc.setStroke(Color.WHITE);
            gc.setLineWidth(1);
            gc.setLineDashes(new double[]{25d, 10d});
            if (road.getOrientation() == Orientation.HORIZONTAL ||
                road.getOrientation() == Orientation.LEFT_HORIZONTAL ||
                road.getOrientation() == Orientation.RIGHT_HORIZONTAL)
            {
                gc.strokeLine(road.getStartCoordinates().x, 
                          road.getStartCoordinates().y + i * constant.LANE_SIZE * constant.PIXELS, 
                          road.getEndCoordinates().x,
                          road.getStartCoordinates().y + i * constant.LANE_SIZE * constant.PIXELS
                          );
            }
            else if (road.getOrientation() == Orientation.VERTICAL ||
                    road.getOrientation() == Orientation.DOWN_VERTICAL ||
                    road.getOrientation() == Orientation.UP_VERTICAL)
            {
                gc.strokeLine(road.getStartCoordinates().x + i * constant.LANE_SIZE * constant.PIXELS, 
                          road.getStartCoordinates().y, 
                          road.getStartCoordinates().x + i * constant.LANE_SIZE * constant.PIXELS,
                          road.getEndCoordinates().y
                          );
            }
            
            
        }
       
        
       }
                       
    }
    
   
    
    //DRAW VEHICLES
    public void drawVehicles()
    {
        
      for (Vehicle vehicle:vehicles)
      {
        if (Car.class.isInstance(vehicle)) 
        //    Double angle = vehicle.getDirectionVector().angleVectorDegree();
        //gc.getFill(BLUE);
          gc.setFill(Color.BLUE);
        else if (Emergency.class.isInstance(vehicle))
           gc.setFill(Color.RED);
        
        gc.fillRect(vehicle.getAxom().x , 
                    vehicle.getAxom().y , 
                    constant.VEHICLE_WIDTH * constant.PIXELS, 
                    constant.VEHICLE_HEIGHT * constant.PIXELS);
      }
      
    }
    
    //DRAW JUNCTION
    public void drawJunctions(){
        //Image image = new Image("images/pattern.png");
        //ImagePattern pattern = new ImagePattern(image);
        
        for (Junction junction:junctions){
            gc.setStroke(Color.YELLOW);
            gc.setFill(Color.BLANCHEDALMOND);
            gc.fillPolygon(new double[] {junction.getCoordinates().get(0).x, 
                                         junction.getCoordinates().get(1).x, 
                                         junction.getCoordinates().get(2).x,
                                         junction.getCoordinates().get(3).x},
                           new double[] {junction.getCoordinates().get(0).y, 
                                         junction.getCoordinates().get(1).y, 
                                         junction.getCoordinates().get(2).y,
                                         junction.getCoordinates().get(3).y},
                           4);
            
               
        }
    }
    //DRAW TRAFFIC LIGHT
    public void drawTrafficLights(){
        
        TrafficLight trafficLight1 = new TrafficLight(roads.get(0), junctions.get(0));
        
        
        for (TrafficLight trafficLight:trafficLights)
        {
            if (trafficLight.getColour() == Color.RED)
                gc.setFill(Color.RED);
            
            if (trafficLight.getColour() == Color.GREEN)
                gc.setFill(Color.GREEN);
            
            if (trafficLight.getColour() == Color.YELLOW)
                gc.setFill(Color.YELLOW);
            
            gc.fillOval(trafficLight.getTrafficLightCoordinate().x , trafficLight.getTrafficLightCoordinate().y - 5, 5, 5);
                
        }
        
        
    }
        
 }
    

    

