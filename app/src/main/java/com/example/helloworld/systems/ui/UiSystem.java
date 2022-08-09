package com.example.helloworld.systems.ui;

import android.graphics.Color;
import android.util.Log;
import com.example.helloworld.components.Viewport;
import com.example.helloworld.components.renderable.RenderablePolygon;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.core.ecs.GameSystem;
import com.example.helloworld.core.observer.GameEvent;
import com.example.helloworld.core.observer.GameEventType;
import com.example.helloworld.core.observer.PublisherHub;
import com.example.helloworld.core.observer.Subscriber;
import com.example.helloworld.systems.render.PolygonFactory;
import org.jbox2d.common.Vec2;

public class UiSystem extends GameSystem implements Subscriber {
    private UiContextState currentState;
    private UiContextState newState;
    private boolean stateChangeNeeded;

    public UiSystem(Coordinator coordinator) {
        super(coordinator);
        stateChangeNeeded = false;
        currentState = UiContextState.NONE;
        newState = UiContextState.NONE;
        PublisherHub.getInstance().subscribe(GameEventType.UI_CONTEXT_CHANGE, this);
    }

    @Override
    public void update(float delta) {
        if (stateChangeNeeded){
            Log.d("Loading", "Changing UI state from " + currentState.name() + " to " + newState.name());

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
        Entity playerViewport = coordinator.getPlayerViewport();

        Entity entity = coordinator.createEntity();
        entity.position = new Vec2(200, 200);
        RenderablePolygon renderablePolygon = PolygonFactory.generateRectangle(100, 100);
        renderablePolygon.zOrder = 1000;
        renderablePolygon.color = Color.BLUE;
        coordinator.addComponent(entity, renderablePolygon);
    }

    private void changeToPauseUi(){
        clearCurrentUi();
    }

    private void changeToLoadingUi(){
        clearCurrentUi();
        Entity playerViewportEntity = coordinator.getPlayerViewport();
        Viewport viewport = (Viewport) coordinator.getComponent(playerViewportEntity, Viewport.class);

        Entity entity = coordinator.createEntity();
        entity.position = new Vec2(viewport.width - 200, viewport.height - 200);
        RenderablePolygon renderablePolygon = PolygonFactory.generateRectangle(100, 100);
        renderablePolygon.zOrder = 1000;
        renderablePolygon.color = Color.YELLOW;
        coordinator.addComponent(entity, renderablePolygon);
    }

    private void changeToMainMenuUi(){
        clearCurrentUi();
    }
}
