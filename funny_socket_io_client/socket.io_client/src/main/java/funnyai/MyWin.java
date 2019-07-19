/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package funnyai;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

/**
 *
 * @author happyli
 */
public class MyWin implements WindowListener {

    public MyWin() {
    }

    @Override
    public void windowOpened(WindowEvent e) {
        
        System.out.println("windowOpened");  
    }

    @Override
    public void windowClosing(WindowEvent e) {
        System.out.println("我关了");  
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {
        
        System.out.println("windowClosed");  
    }

    @Override
    public void windowIconified(WindowEvent e) {
        
        System.out.println("windowIconified");  
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
        
        System.out.println("windowDeiconified");  
    }

    @Override
    public void windowActivated(WindowEvent e) {
        System.out.println("windowActivated");
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
        System.out.println("windowDeactivated");  
    }
    
}
