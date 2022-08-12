package com.example.helloworld.systems.ui;

import android.util.Log;
import com.example.helloworld.components.Ui;
import com.example.helloworld.components.renderable.Renderable;
import com.example.helloworld.core.CoordinateSystem;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.Entity;
import com.example.helloworld.systems.render.PolygonFactory;
import org.jbox2d.common.Vec2;

public class ButtonFactory {
    public static Entity createRectangularButton(Coordinator coordinator, RectangularButtonInfo buttonInfo){
        Entity newButton = coordinator.createEntity();
        newButton.position = buttonInfo.center;
        Renderable renderablePolygon = PolygonFactory.generateRectangle(buttonInfo.width, buttonInfo.height, CoordinateSystem.SCREEN);
        renderablePolygon.zOrder = buttonInfo.zOrder;
        renderablePolygon.color = buttonInfo.color;
        renderablePolygon.isScreenElement = true;
        coordinator.addComponent(newButton, renderablePolygon);
        Ui uiComponent = new Ui();
        uiComponent.height = buttonInfo.height;
        uiComponent.width = buttonInfo.width;
        uiComponent.onActivate = buttonInfo.onActivate;
        uiComponent.onDeactivate = buttonInfo.onDeactivate;
        uiComponent.onDrag = buttonInfo.onDrag;
        uiComponent.containsCoordinate = coordinate -> {
            float left = newButton.position.x - uiComponent.width / 2f;
            float top = newButton.position.y - uiComponent.height / 2f;
            float right = newButton.position.x + uiComponent.width / 2f;
            float bottom = newButton.position.y + uiComponent.height / 2f;
            boolean result = left < coordinate.x && top < coordinate.y && right > coordinate.x && bottom > coordinate.y;
            //Log.d("Input", "Contains coordinate: " + result);
            return left < coordinate.x && top < coordinate.y && right > coordinate.x && bottom > coordinate.y;
        };
        coordinator.addComponent(newButton, uiComponent);
        Log.d("Loading", "new button: " + newButton.id + ", sig: " + newButton.signature);

        return newButton;
    }

    public static class RectangularButtonInfo {
        public Vec2 center;
        public int width;
        public int height;
        public int zOrder;
        public int color;
        public IOnActivate onActivate;
        public IOnDeactivate onDeactivate;
        public IOnDrag onDrag;

        public RectangularButtonInfo(Vec2 center, int width, int height, int zOrder, int color, IOnActivate onActivate, IOnDeactivate onDeactivate, IOnDrag onDrag) {
            this.center = center;
            this.width = width;
            this.height = height;
            this.zOrder = zOrder;
            this.color = color;
            this.onActivate = onActivate;
            this.onDeactivate = onDeactivate;
            this.onDrag = onDrag;
        }
    }
}
