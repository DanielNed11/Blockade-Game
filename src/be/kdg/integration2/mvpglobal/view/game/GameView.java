package be.kdg.integration2.mvpglobal.view.game;

import be.kdg.integration2.mvpglobal.model.*;
import be.kdg.integration2.mvpglobal.view.CustomAlerts;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.transform.Rotate;

public class GameView extends StackPane {
    private final int DEVIATION_DICE = 10;

    private GridPane board2D;
    private GridPane board3D;
    private Slider rotationSlider;
    private Label name;
    private Label movementPoints1;
    private Label movementPoints2;
    private StackPane blackPiecesView;
    private Button exitButton;
    private StackPane die1;
    private StackPane die2;
    private Button endTurnButton;
    private Button undoButton;

    public GameView() {
        initializeNodes();
        layoutNodes();
    }

    private void initializeNodes() {
        board2D = new GridPane();

        blackPiecesView = new StackPane();

        VBox board2DContainer = new VBox(blackPiecesView, board2D);
        board2DContainer.setMaxSize(200, 200);
        board2DContainer.setAlignment(Pos.CENTER);
        board2DContainer.setSpacing(20);

        setAlignment(board2DContainer, Pos.BOTTOM_LEFT);

        board3D = new GridPane();

        rotationSlider = new Slider(0, 360, 0);
        rotationSlider.setBlockIncrement(5);
        rotationSlider.setMaxWidth(0);

        board3D.rotateProperty().bind
                (rotationSlider.valueProperty());

        endTurnButton = new Button("End Turn");

        undoButton = new Button("Undo");

        PerspectiveCamera camera = new PerspectiveCamera();
        camera.setTranslateZ(-500);
        camera.getTransforms().add(new Rotate(45, Rotate.X_AXIS));

        Group container3D = new Group(board3D);

        container3D.setTranslateY(-350);
        container3D.setTranslateX(100);

        SubScene subScene = new SubScene(container3D, 600,500, true, SceneAntialiasing.BALANCED);
        subScene.setCamera(camera);
        subScene.setTranslateY(0);
        subScene.maxHeight(600);
        subScene.maxWidth(500);

        exitButton = new Button("Exit");

        name = new Label();
        name.getStyleClass().add("label-name");

        movementPoints1 = new Label();
        movementPoints2 = new Label();

        die1 = new StackPane();
        die2 = new StackPane();

        HBox diceContainer = new HBox(die1, die2);
        diceContainer.setSpacing(15);
        diceContainer.setMaxSize(20, 20);
        setAlignment(diceContainer, Pos.BOTTOM_RIGHT);

        VBox rightSide = new VBox(movementPoints1, movementPoints2, diceContainer, undoButton, endTurnButton);
        rightSide.setAlignment(Pos.CENTER);
        rightSide.setSpacing(30);
        setAlignment(rightSide, Pos.CENTER_RIGHT);
        rightSide.setMaxSize(20, 400);
        rightSide.setTranslateX(-50);

        getChildren().addAll(board2DContainer,rightSide, subScene);
    }

    private void layoutNodes() {
        setAlignment(name, Pos.TOP_LEFT);

        name.setTranslateY(55);
        name.setTranslateX(20);

        movementPoints1.getStyleClass().add("points-label");
        movementPoints2.getStyleClass().add("points-label");

        rotationSlider.setTranslateX(1000);

        double size = 37.5 * 5;
        board2D.setMaxSize(size, size);
        board2D.setHgap(3);
        board2D.setVgap(3);

        blackPiecesView.setMaxSize(37.5, 37.5);

        board3D.setHgap(3);
        board3D.setVgap(3);

        setAlignment(exitButton, Pos.BOTTOM_CENTER);
        exitButton.setTranslateY(-100);

        die1.setMaxSize(60,60);
        die2.setMaxSize(60,60);

        getChildren().addAll(name, rotationSlider, exitButton);
    }

    Button getExitButton() {
        return exitButton;
    }

    StackPane getBlackPiecesView() {
        return blackPiecesView;
    }

    GridPane getBoard2D() {
        return board2D;
    }

    GridPane getBoard3D() {
        return board3D;
    }

    Slider getRotationSlider() {
        return rotationSlider;
    }

    Label getName() {
        return name;
    }

    Label getMovementPoints1() {
        return movementPoints1;
    }

    Label getMovementPoints2() {
        return movementPoints2;
    }

    StackPane getDie1() {
        return die1;
    }

    StackPane getDie2() {
        return die2;
    }

    Button getEndTurnButton() {
        return endTurnButton;
    }

    Button getUndoButton() {
        return undoButton;
    }

    void setUpBlackPieces(BlackPieces blackPieces, StackPane blackPieces2D) {
        blackPieces2D.getChildren().clear();

        StackPane tileView = new StackPane();
        double size = 50 * 0.75;
        tileView.setPrefSize(size, size);

        for (Piece piece : blackPieces.getBlackPieces().getPieces()) {
            createPiece(tileView, piece);
        }
        blackPieces2D.getChildren().add(tileView);

    }

    void setUp2DBoard(Board board, GridPane board2D) {
        int boardSize = board.getSize();
        board2D.getChildren().clear();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {
                StackPane tileView = new StackPane();

                double tileSize = 50 * 0.75;
                tileView.setPrefSize(tileSize, tileSize);

                for (Piece piece : board.getTile(row, col).getPieces()) {
                    createPiece(tileView, piece);
                }

                board2D.add(tileView, row, col);
            }
        }
    }

    private void createPiece(StackPane tileView, Piece piece) {
        StackPane pieceView = new StackPane();

        double pieceSize = piece.getSize().getSize() * 0.75;

        Rectangle rect = new Rectangle(pieceSize, pieceSize);
        rect.setFill(piece.getColor().getColor());
        rect.setStroke(Color.BLACK);
        rect.setStrokeWidth(1);

        pieceView.getChildren().addAll(rect);

        tileView.getChildren().add(pieceView);
    }

    void setUp3DBoard(Board board, GridPane board3D) {
        int boardSize = board.getSize();
        board3D.getChildren().clear();

        for (int row = 0; row < boardSize; row++) {
            for (int col = 0; col < boardSize; col++) {

                StackPane tile = new StackPane();
                tile.setPrefSize(75, 75);

                for (Piece piece : board.getTile(row, col).getPieces()) {
                    StackPane pieceView = new StackPane();
                    int pieceSize = piece.getSize().getSize();

                    Box box = new Box(pieceSize * 1.5, pieceSize * 1.5, 15);
                    PhongMaterial material = new PhongMaterial();

                    material.setDiffuseColor(piece.getColor().getColor());
                     material.setSpecularColor(Color.WHITE);

                    box.setMaterial(material);

                    pieceView.getChildren().add(box);
                    pieceView.setTranslateZ(piece.getTranslateZ());

                    tile.getChildren().add(pieceView);
                }

                board3D.add(tile, row, col);
            }
        }
    }

    //method that draws the dies
    StackPane initializeDice(ElementColors color, int value) {
        StackPane diceContainer = new StackPane();

        Rectangle rectangle = new Rectangle(60,60);
        rectangle.setFill(color.getColor());
        rectangle.setStroke(Color.WHITE);
        rectangle.setStrokeWidth(3);
        rectangle.setArcWidth(25);
        rectangle.setArcHeight(25);

        diceContainer.getChildren().add(rectangle);
        drawDots(diceContainer, value);

        return diceContainer;
    }

    private void drawDots(StackPane die, int value) {
        switch (value) {
            case 1:
                drawOne(die);
                break;
            case 2:
                drawTwo(die);
                break;
            case 3:
                drawOne(die);
                drawTwo(die);
                break;
            case 4:
                drawFour(die);
                break;
            case 5:
                drawOne(die);
                drawFour(die);
                break;
            case 6:
                drawSix(die);
                break;
            default:
                CustomAlerts.alert("Rolling dice error", "Error",
                        "Error while rolling the dice.", Alert.AlertType.ERROR);
                break;
        }


    }

    private void drawOne(StackPane die) {
        Circle circle = new Circle(5, Color.WHITE);
        die.getChildren().add(circle);
        setAlignment(circle, Pos.CENTER);
    }

    private void drawTwo(StackPane die) {
        Circle circleOne = new Circle(5, Color.WHITE);
        Circle circleTwo = new Circle(5, Color.WHITE);
        setAlignment(circleOne, Pos.TOP_LEFT);
        circleOne.setTranslateX(DEVIATION_DICE);
        circleOne.setTranslateY(DEVIATION_DICE);
        setAlignment(circleTwo, Pos.BOTTOM_RIGHT);
        circleTwo.setTranslateX(-DEVIATION_DICE);
        circleTwo.setTranslateY(-DEVIATION_DICE);
        die.getChildren().addAll(circleOne, circleTwo);
    }

    private void drawFour(StackPane die) {
        drawTwo(die);
        Circle circleOne = new Circle(5, Color.WHITE);
        Circle circleTwo = new Circle(5, Color.WHITE);
        setAlignment(circleOne, Pos.BOTTOM_LEFT);
        circleOne.setTranslateX(DEVIATION_DICE);
        circleOne.setTranslateY(-DEVIATION_DICE);
        setAlignment(circleTwo, Pos.TOP_RIGHT);
        circleTwo.setTranslateX(-DEVIATION_DICE);
        circleTwo.setTranslateY(DEVIATION_DICE);
        die.getChildren().addAll(circleOne, circleTwo);
    }

    private void drawSix(StackPane die) {
        drawFour(die);
        Circle circleOne = new Circle(5, Color.WHITE);
        Circle circleTwo = new Circle(5, Color.WHITE);
        setAlignment(circleOne, Pos.CENTER_LEFT);
        circleOne.setTranslateX(DEVIATION_DICE);
        circleOne.setTranslateY(0);
        setAlignment(circleTwo, Pos.CENTER_RIGHT);
        circleTwo.setTranslateX(-DEVIATION_DICE);
        circleTwo.setTranslateY(0);
        die.getChildren().addAll(circleOne, circleTwo);
    }
}
