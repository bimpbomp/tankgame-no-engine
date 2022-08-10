package com.example.helloworld.systems.ui;

import android.graphics.Color;
import android.util.Log;
import com.example.helloworld.components.Ui;
import com.example.helloworld.components.Viewport;
import com.example.helloworld.components.renderable.RenderablePolygon;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.GameSystem;
import com.example.helloworld.core.observer.*;
import com.example.helloworld.systems.render.PolygonFactory;
import org.jbox2d.common.Vec2;

public class UiSystem extends GameSystem implements ISubscriber {
    private UiContextState currentState;
    private UiContextState newState;
    private boolean stateChangeNeeded;
    private Publisher uiStatePublisher;

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

        Entity entity = coordinator.createEntity();
        entity.position = new Vec2(200, 200);
        RenderablePolygon renderablePolygon = PolygonFactory.generateRectangle(100, 100);
        renderablePolygon.zOrder = 1000;
        renderablePolygon.color = Color.BLUE;
        renderablePolygon.isScreenElement = true;
        coordinator.addComponent(entity, renderablePolygon);
        Ui uiComponent = new Ui();
        uiComponent.height = 100;
        uiComponent.width = 100;
        uiComponent.onActivate = () -> Log.d("UI", "pause button onActivate");
        uiComponent.onDeactivate = () -> {
            Log.d("UI", "changing from level to pause");
            uiStatePublisher.notify(new UiContextChangeEvent(UiContextState.LEVEL, UiContextState.PAUSE_MENU));
        };
        coordinator.addComponent(entity, uiComponent);
    }

    private void changeToPauseUi(){
        clearCurrentUi();
        Entity playerViewportEntity = coordinator.getPlayerViewport();
        Viewport viewport = (Viewport) coordinator.getComponent(playerViewportEntity, Viewport.class);

        Entity entity = coordinator.createEntity();
        entity.position = new Vec2(viewport.width - 200, viewport.height - 200);
        RenderablePolygon renderablePolygon = PolygonFactory.generateRectangle(100, 100);
        renderablePolygon.zOrder = 1000;
        renderablePolygon.color = Color.YELLOW;
        renderablePolygon.isScreenElement = true;
        coordinator.addComponent(entity, renderablePolygon);
        Ui uiComponent = new Ui();
        uiComponent.height = 100;
        uiComponent.width = 100;
        uiComponent.onActivate = () -> Log.d("UI", "un-pause button onActivate");
        uiComponent.onDeactivate = () -> {
            Log.d("UI", "changing from pause to level");
            uiStatePublisher.notify(new UiContextChangeEvent(UiContextState.PAUSE_MENU, UiContextState.LEVEL));
        };
        coordinator.addComponent(entity, uiComponent);
    }

    private void changeToLoadingUi(){
        clearCurrentUi();
    }

    private void changeToMainMenuUi(){
        clearCurrentUi();
    }
}
