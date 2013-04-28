/**
 * \file Animation.java
 * 
 * \author Robert Fletcher
 * 
 * \date 25/04/2013
 * 
 * \brief This class contains the UI for the slideshow and also the arraylist containing all the slides. 
 *  to run the silde show instantiate this class use the method addBiCharts to add 2
 * charts into a slide for the slide show. the set this class to visible and click the begin button
 * 
 */

package animation;

import static animation.AnimationType.LEFT;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Animation extends JFrame {
    private ArrayList<Slide> slide = new ArrayList<Slide>();
    private boolean run;
    private JPanel main;
    private int pos;
    private AnimationSpeed speed; 
    private AnimationWait wait;
    private JButton pause,next ,pre;
    private final int ICONSIZE = 40;
    private final double WEIGHT = 0.5;
    private boolean start;
    private final Dimension BUTTONSIZE = new Dimension(50,50);
    private Thread t;
    private int i;
    private SlideLeft s; 
    /**
     * Constructor that sets up the main ui
     */
    public Animation(){
        i = 0;
        this.setExtendedState(this.getExtendedState() | JFrame.MAXIMIZED_BOTH);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        main = new JPanel();
        start = false;
        main.setLayout(null);
        
        main.setBackground(Color.DARK_GRAY);
        JPanel buttonP = new JPanel();
        this.setLayout(new BorderLayout());
        buttonP.setLayout(new FlowLayout());
        try {
            next = new JButton(new ImageIcon(ImageIO.read(this.getClass().getResource("/assets/images/next.png")).getScaledInstance(ICONSIZE, ICONSIZE, Image.SCALE_SMOOTH)));
            pause = new JButton(new ImageIcon(ImageIO.read(this.getClass().getResource("/assets/images/play.png")).getScaledInstance(ICONSIZE, ICONSIZE, Image.SCALE_SMOOTH)));
            pre = new JButton(new ImageIcon(ImageIO.read(this.getClass().getResource("/assets/images/pre.png")).getScaledInstance(ICONSIZE, ICONSIZE, Image.SCALE_SMOOTH)));
        } catch (IOException ex) {}
        next.setPreferredSize(BUTTONSIZE);
        next.setToolTipText("Next Slide");
        
        pause.setPreferredSize(BUTTONSIZE);
        pause.setToolTipText("Play");
        
        pre.setPreferredSize(BUTTONSIZE);
        pre.setToolTipText("Previous Slide");
        
        
        buttonP.add(pre);
        buttonP.add(pause);
        buttonP.add(next);
        this.add(main, BorderLayout.CENTER);
        this.add(buttonP, BorderLayout.SOUTH);
        this.validate();
        
        JPanel a1 = new JPanel();
        JPanel a2 = new JPanel();
        JPanel a3 = new JPanel();
        JPanel a4 = new JPanel();
        JPanel a5 = new JPanel();
        JPanel a6 = new JPanel();
        
        
        a1.setBackground(Color.GREEN);
        a2.setBackground(Color.BLUE);
        
        a3.setBackground(Color.GREEN);
        a4.setBackground(Color.BLUE);
        
        a5.setBackground(Color.GREEN);
        a6.setBackground(Color.BLUE);
        
        
        a1.validate();
         a2.validate();
          a3.validate();
           a4.validate();
            a5.validate();
             a5.validate();
            

        
        
        //---------------add listeners -----------/
          pre.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                i = i-2;
                s.interupt();
                             
                run = true;
            }
        });
             
        next.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                
                i++;
                s.interupt();
                pause.setText("Pause");
               
                run = true;
            }
        });
        pause.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                if(run){
                    try {
                        run = false;
                        pause.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource("/assets/images/play.png")).getScaledInstance(ICONSIZE, ICONSIZE, Image.SCALE_SMOOTH)));
                        slide.get(pos).pause();
                    } catch (IOException ex) {}
                }else{
                    if(!start){
                        t = new slideshow();
                        t.start();
                        start = true;
                    }else{
                        slide.get(pos).resume(); 
                    }
                    
                    
                    run = true;
                    try {
                        pause.setIcon(new ImageIcon(ImageIO.read(this.getClass().getResource("/assets/images/pause.png")).getScaledInstance(ICONSIZE, ICONSIZE, Image.SCALE_SMOOTH)));
                    } catch (IOException ex) {}
                   
                }
                
            }
        });   
        
    }
    /**
     * Function to add a slide to the slide show. It will take in 2 charts and add the slide to the Main ui,
     * @param ch1 Chart on the left side
     * @param ch2 Chart on the right side
     * @param t Animation Type
     * @param s Animation Slide Speed
     * @param w Animation wait time
     * @return Boolean true if sucsessful
     */
    public boolean addBiCharts(JPanel ch1,JPanel ch2,AnimationType t,AnimationSpeed s,AnimationWait w){

        JPanel c = new JPanel();
        c.setLayout(new GridLayout(1,2));
        c.add(ch1);
        c.add(ch2);
        c.validate();
        c.repaint();
      
        slide.add(new SlideLeft(c,main,s,w));    
        main.add(c);
        main.revalidate();
        main.repaint();
        return true;
    }
    /**
     * Remove a slide from the slideshow
     * @param pos position in the Slideshow list. 
     */
    public boolean removeSlide(int pos){
        slide.remove(pos);
        return true;
    }

    /**
     * method to set the animation slide in and out speed
     * @param Animation speed
     */
    public boolean setSpeed(AnimationSpeed s){
        speed = s;
        return true;
    }
    /**
     * Set the animation wait time once the slide has been slid in
     * @param AnimationWait 
     */
    public boolean setWait(AnimationWait w){
        wait = w;
        return true;
    }
    
    /**
     * Private class to start the slide show.
     */
    private class slideshow extends Thread{
        
        public void run(){
            for(i = 0; i<slide.size();i++){
               Slide sld = null; 
               switch(slide.get(i).getAnimationType()){
                    case LEFT:
                            sld = slide.get(i);
                            
                            s = (SlideLeft) sld;
                            s.setSpeed(speed);
                            s.setWait(wait);
                            s.start();
                            main.updateUI();
                            System.out.println("Upadte main ui");
                            main.updateUI();
                            pos = i;
                            
                            break;
                } 
               while(sld.getThread().isAlive()){}
               
            }
            JOptionPane.showMessageDialog(null, "The slide show has ended", 
                        "The End", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    public static void main(String[]args){
        Animation a = new Animation();
        a.setVisible(true);
    }

}
