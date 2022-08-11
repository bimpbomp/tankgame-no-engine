package com.example.helloworld.components;

import com.example.helloworld.core.ecs.Component;
import com.example.helloworld.systems.ui.IContainsCoordinate;
import com.example.helloworld.systems.ui.IOnActivate;
import com.example.helloworld.systems.ui.IOnDeactivate;
import com.example.helloworld.systems.ui.IOnDrag;

public class Ui extends Component {
    public int width = 0;
    public int height = 0;
    public int pointerId = -1;
    public IOnActivate onActivate;
    public IOnDeactivate onDeactivate;
    public IOnDrag onDrag;
    public IContainsCoordinate containsCoordinate;
}
