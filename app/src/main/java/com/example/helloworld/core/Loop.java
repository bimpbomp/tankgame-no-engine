package com.example.helloworld.core;

import android.util.Log;
import com.example.helloworld.components.PhysicsComponent;
import com.example.helloworld.components.TankInput;
import com.example.helloworld.components.Ui;
import com.example.helloworld.components.Viewport;
import com.example.helloworld.components.renderable.Renderable;
import com.example.helloworld.core.android.GameSurface;
import com.example.helloworld.core.ecs.Component;
import com.example.helloworld.core.ecs.Coordinator;
import com.example.helloworld.core.ecs.GameSystem;
import com.example.helloworld.systems.input.GameInputSystem;
import com.example.helloworld.systems.level.LevelSystem;
import com.example.helloworld.systems.movement.TankMovementSystem;
import com.example.helloworld.systems.physics.PhysicsSystem;
import com.example.helloworld.systems.render.RenderSystem;
import com.example.helloworld.systems.ui.UiSystem;
import com.example.helloworld.systems.viewport.ViewportSystem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Loop implements Runnable {
    private boolean gameIsRunning;
    private Coordinator coordinator;
    private RenderSystem renderSystem;
    private List<OrderedGameSystem> systems;
    public int nextSystemPriority;

    public Loop() {
        gameIsRunning = false;
    }

    public void initialiseGame(GameSurface gameSurface) {
        coordinator = new Coordinator();
        systems = new ArrayList<>();
        nextSystemPriority = 0;

        // register component types
        registerNewComponentType(PhysicsComponent.class);
        registerNewComponentType(Renderable.class);
        registerNewComponentType(Viewport.class);
        registerNewComponentType(Ui.class);
        registerNewComponentType(TankInput.class);

        // register render system - treated differently as it's executed differently
        renderSystem = new RenderSystem(coordinator, gameSurface);
        coordinator.registerSystem(renderSystem);

        //register other systems in decreasing priority order
        registerNewSystem(new LevelSystem(coordinator)); // creates player
        registerNewSystem(new GameInputSystem(coordinator)); // gets inputs for UI and TankMovement systems
        registerNewSystem(new UiSystem(coordinator)); // needs player to exists
        registerNewSystem(new TankMovementSystem(coordinator));
        registerNewSystem(new PhysicsSystem(coordinator));
        registerNewSystem(new ViewportSystem(coordinator));

        renderSystem.init();
        systems.forEach(orderedGameSystem -> {
            Log.d("Loading", "Initialising system: " + orderedGameSystem.system.getClass().getSimpleName());
            orderedGameSystem.system.init();
        });
    }

    // Loop taken from: https://dewitters.com/dewitters-gameloop/
    @Override
    public void run() {
        int TICKS_PER_SECOND = 25;
        int SKIP_TICKS = 1000 / TICKS_PER_SECOND;
        int MAX_FRAMESKIP = 5;

        long nextGameTick = System.currentTimeMillis();
        int loops;
        float interpolation;

        Log.d("Loading", "Game loop starting");
        gameIsRunning = true;
        while (gameIsRunning) {
            loops = 0;

            while (System.currentTimeMillis() > nextGameTick && loops < MAX_FRAMESKIP) {
                updateGame(1f / TICKS_PER_SECOND);

                nextGameTick += SKIP_TICKS;
                loops++;
            }

            interpolation = (float) (System.currentTimeMillis() + SKIP_TICKS - nextGameTick) / (float) SKIP_TICKS;
            renderSystem.update(interpolation);
        }
    }

    public void updateGame(float delta){
        systems.forEach(orderedGameSystem -> orderedGameSystem.system.update(delta));
    }

    private void registerNewComponentType(Class clazz){
        coordinator.registerComponentType(Component.getType(clazz));
        Log.d("Loading", "New component type '" + clazz.getSimpleName() + "' id: " + Component.getType(clazz));
    }

    private void registerNewSystem(GameSystem system){
        coordinator.registerSystem(system);
        systems.add(new OrderedGameSystem(nextSystemPriority++, system));
        Collections.sort(systems);
        Log.d("Loading", "New system type '" + system.getClass().getSimpleName());
    }

    public void setGameIsRunning(boolean isRunning){
        this.gameIsRunning = isRunning;
    }

    private static class OrderedGameSystem implements Comparable<OrderedGameSystem>{
        public int priority;
        public GameSystem system;

        public OrderedGameSystem(int priority, GameSystem system) {
            this.priority = priority;
            this.system = system;
        }

        @Override
        public int compareTo(OrderedGameSystem orderedGameSystem) {
            return Integer.compare(priority, orderedGameSystem.priority);
        }
    }
}
