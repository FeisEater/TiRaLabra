
package tiralabra.gui;

import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import tiralabra.VertexContainer;
import tiralabra.gui.geometrytools.ChainPolygon;
import tiralabra.gui.geometrytools.FreeDraw;
import tiralabra.gui.geometrytools.InsertPoint;
import tiralabra.gui.geometrytools.JoinPolygons;
import tiralabra.gui.geometrytools.SetEndPoints;
import tiralabra.gui.geometrytools.MoveOrDeletePolygon;
import tiralabra.gui.geometrytools.MoveOrDeletePoint;

/**
 *
 * @author Pavel
 */
public class ToolSwitcher implements KeyEventDispatcher {
    private VertexContainer vertices;
    private GraphicInterface gui;
    public ToolSwitcher(VertexContainer v, GraphicInterface g)
    {
        vertices = v;
        gui = g;
        KeyboardFocusManager manager = 
                KeyboardFocusManager.getCurrentKeyboardFocusManager();
        manager.addKeyEventDispatcher(this);
        System.out.println("C - chain polygons mode.");
        System.out.println("F - free draw mode.");
        System.out.println("S - set end points mode.");
    }

    public void keyReleased(KeyEvent e)
    {
        switch (e.getKeyCode())
        {
            case KeyEvent.VK_C:
                gui.switchTool(new ChainPolygon(vertices, gui));
                break;
            case KeyEvent.VK_F:
                gui.switchTool(new FreeDraw(vertices, gui));
                break;
            case KeyEvent.VK_S:
                gui.switchTool(new SetEndPoints(vertices, gui));
                break;
            case KeyEvent.VK_D:
                gui.switchTool(new MoveOrDeletePolygon(vertices, gui));
                break;
            case KeyEvent.VK_E:
                gui.switchTool(new MoveOrDeletePoint(vertices, gui));
                break;
            case KeyEvent.VK_A:
                gui.switchTool(new JoinPolygons(vertices, gui));
                break;
            case KeyEvent.VK_Q:
                gui.switchTool(new InsertPoint(vertices, gui));
                break;
            default:
                break;
        }
        
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent e)
    {
        if (e.getID() == KeyEvent.KEY_RELEASED)
            keyReleased(e);
        return false;
    }
}
