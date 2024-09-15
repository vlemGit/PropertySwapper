package com.vlemgit.controller;

public class VisualizerController {

/*    Pane root = new Pane();

    Button node1 = new Button("Node 1");
    Button node2 = new Button("Node 2");

        node1.setLayoutX(100);
        node1.setLayoutY(100);
        node2.setLayoutX(300);
        node2.setLayoutY(200);

    // Create a connection (curved line)
    CubicCurve curve = new CubicCurve();
        curve.setStartX(node1.getLayoutX() + node1.getWidth());
        curve.setStartY(node1.getLayoutY() + node1.getHeight() / 2);
        curve.setControlX1(node1.getLayoutX() + 150);
        curve.setControlY1(node1.getLayoutY() - 50);
        curve.setControlX2(node2.getLayoutX() - 150);
        curve.setControlY2(node2.getLayoutY() + 50);
        curve.setEndX(node2.getLayoutX());
        curve.setEndY(node2.getLayoutY() + node2.getHeight() / 2);
        curve.setStroke(Color.BLACK);
        curve.setFill(null);

    // Add dragging behavior to node1
        node1.setOnMouseDragged(event -> {
        node1.setLayoutX(event.getSceneX() - node1.getWidth() / 2);
        node1.setLayoutY(event.getSceneY() - node1.getHeight() / 2);

        // Update the curve when node1 is dragged
        curve.setStartX(node1.getLayoutX() + node1.getWidth());
        curve.setStartY(node1.getLayoutY() + node1.getHeight() / 2);
        curve.setControlX1(node1.getLayoutX() + 150);
        curve.setControlY1(node1.getLayoutY() - 50);
    });

    // Add dragging behavior to node2
        node2.setOnMouseDragged(event -> {
        node2.setLayoutX(event.getSceneX() - node2.getWidth() / 2);
        node2.setLayoutY(event.getSceneY() - node2.getHeight() / 2);

        // Update the curve when node2 is dragged
        curve.setEndX(node2.getLayoutX());
        curve.setEndY(node2.getLayoutY() + node2.getHeight() / 2);
        curve.setControlX2(node2.getLayoutX() - 150);
        curve.setControlY2(node2.getLayoutY() + 50);
    });

        root.getChildren().addAll(curve, node1, node2);*/
    
}
