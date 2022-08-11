package com.example.helloworld.systems.ui;

import android.graphics.Color;
import android.util.Log;
import com.example.helloworld.components.Viewport;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.GameSystem;
import com.example.helloworld.core.observer.*;
import org.jbox2d.common.Vec2;

public class UiSystem extends GameSystem implements ISubscriber {
    private UiContextState currentState;
    private UiContextState newState;
    private boolean stateChangeNeeded;
    private Publisher uiStatePublisher;

    private boolean upPressed = false;
    private boolean leftPressed = false;
    private boolean rightPressed = false;
    private boolean downPressed = false;

    public UiSystem(Coordinator coordinator) {
        super(coordinator);
        stateChangeNeeded = true;
        newState = UiContextState.LEVEL;
        currentState = UiContextState.NONE;
        PublisherHub.getInstance().subscribe(GameEventType.UI_CONTEXT_CHANGE, this);
        uiStatePublisher = new Publisher();
        PublisherHub.getInstance().addPublisher(GameEventType.UI_CONTEXT_CHANGE, uiStatePublisher);
    }

    @Override
    public void update(float delta) {
        if (stateChangeNeeded){
            Log.d("UI", "Changing UI state from " + currentState.name() + " to " + newState.name());

            //process state change
            switch (newState){
                case MAIN_MENU:
                    changeToMainMenuUi();
                    break;
                case LOADING:
                    changeToLoadingUi();
                    break;
                case LEVEL:
                    changeToLevelUi();
                    break;
                case PAUSE_MENU:
                    changeToPauseUi();
                    break;
                case NONE:
                    clearCurrentUi();
                    break;
            }
            stateChangeNeeded = false;
        }

        Vec2 playerMovementInput = new Vec2();
        int x;
        int y;
        if (leftPressed && !rightPressed){
            x = -1;
        } else if (rightPressed && !leftPressed){
            x = 1;
        } else {
            x = 0;
        }

        if (upPressed && !downPressed){
            y = -1;
        } else if (downPressed && !upPressed){
            y = 1;
        } else {
            y = 0;
        }

        playerMovementInput.set(x, y);
        playerMovementInput.normalize();
        Log.d("Ui", "Input vector: " + playerMovementInput + " len: " + playerMovementInput.length());
    }

    @Override
    public void onNotify(GameEvent event) {
        UiContextChangeEvent uiContextChangeEvent = (UiContextChangeEvent) event;
        currentState = uiContextChangeEvent.from;
        newState = uiContextChangeEvent.to;
        stateChangeNeeded = true;
    }

    private void clearCurrentUi(){
        for (Entity entity : entities){
            coordinator.destroyEntity(entity);
        }
    }

    private void changeToLevelUi(){
        clearCurrentUi();
        Entity playerViewportEntity = coordinator.getPlayerViewport();
        Viewport viewport = (Viewport) coordinator.getComponent(playerViewportEntity, Viewport.class);

        Vec2 dpadCenter = new Vec2(300, viewport.height - 300);
        int dpadSize = 300;

        ButtonFactory.createRectangularButton(coordinator, new ButtonFactory.RectangularButtonInfo(
                new Vec2(dpadCenter.x - dpadSize / 3f, dpadCenter.y),
                dpadSize / 3,
                dpadSize / 3,
                100,
                Color.YELLOW,
                () -> {
                    Log.d("UI", "left button onActivate");
                    leftPressed = true;
                },
                () -> {
                    Log.d("UI", "left button onDeactivate");
                    leftPressed = false;
                },
                () -> {
                    Log.d("UI", "left button onDrag");
                }
        ));

        ButtonFactory.createRectangularButton(coordinator, new ButtonFactory.RectangularButtonInfo(
                new Vec2(dpadCenter.x, dpadCenter.y - dpadSize / 3f),
                dpadSize / 3,
                dpadSize / 3,
                100,
                Color.BLUE,
                () -> {
                    Log.d("UI", "up button onActivate");
                    upPressed = true;
                },
                () -> {
                    Log.d("UI", "up button onDeactivate");
                    upPressed = false;
                },
                () -> {
                    Log.d("UI", "up button onDrag");
                }
        ));

        ButtonFactory.createRectangularButton(coordinator, new ButtonFactory.RectangularButtonInfo(
                new Vec2(dpadCenter.x, dpadCenter.y + dpadSize / 3f),
                dpadSize / 3,
                dpadSize / 3,
                100,
                Color.GREEN,
                () -> {
                    Log.d("UI", "down button onActivate");
                    downPressed = true;
                },
                () -> {
                    Log.d("UI", "down button onDeactivate");
                    downPressed = false;
                },
                () -> {
                    Log.d("UI", "down button onDrag");
                }
        ));

        ButtonFactory.createRectangularButton(coordinator, new ButtonFactory.RectangularButtonInfo(
                new Vec2(dpadCenter.x + dpadSize / 3f, dpadCenter.y),
                dpadSize / 3,
                dpadSize / 3,
                100,
                Color.RED,
                () -> {
                    Log.d("UI", "right button onActivate");
                    rightPressed = true;
                },
                () -> {
                    Log.d("UI", "right button onDeactivate");
                    rightPressed = false;
                },
                () -> {
                    Log.d("UI", "right button onDrag");
                }
        ));
    }

    private void changeToPauseUi(){
        clearCurrentUi();
//        Entity playerViewportEntity = coordinator.getPlayerViewport();
//        Viewport viewport = (Viewport) coordinator.getComponent(playerViewportEntity, Viewport.class);
//
//        ButtonFactory.RectangularButtonInfo buttonInfo = new ButtonFactory.RectangularButtonInfo(
//                new Vec2(viewport.width - 200, viewport.height - 200),
//                100,
//                100,
//                100,
//                Color.BLUE,
//                () -> Log.d("UI", "un-pause button onActivate"),
//                () ->{
//                        Log.d("UI", "changing from pause to level");
//                        uiStatePublisher.notify(new UiContextChangeEvent(UiContextState.PAUSE_MENU, UiContextState.LEVEL));
//                }
//        );
//        Entity newButton = ButtonFactory.createRectangularButton(coordinator, buttonInfo);
    }

    private void changeToLoadingUi(){
        clearCurrentUi();
    }

    private void changeToMainMenuUi(){
        clearCurrentUi();
    }

}
