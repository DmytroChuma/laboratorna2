/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package snowflake.koch;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import javax.swing.JApplet;
import javax.swing.JFrame;

/**
 *
 * @author Admin
 */
public class SnowflakeKoch extends JApplet {
    private boolean drawn = false;
    private Graphics2D g2 = null;
    int Iter = 4; //Кількість ітерацій
    
     @Override
    public void paint(Graphics g) {
        if (drawn)
            return;
        drawn = true;
 
        super.paint(g);        
 
        g2 = (Graphics2D) g;
        
        g2.setColor(Color.blue); 
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
 
        /* Розрахунок координат трикутника для промальовування сніжинки Коха */
        double a = 500; // Довжина сторони трикутника (px)
        double p1x = 150; // Координата Х нижньої лівої точки основи трикутника (px)
        double p1y = 550; // Координата У нижньої лівої точки основи трикутника (px)
        double p2x = p1x + a;  // Координата Х правої точки основи трикутника
        double p2y = p1y;   // Координата У правої точки основи трикутника
        double h = Math.sqrt(Math.pow(a, 2) - Math.pow((a / 2), 2) / 4); //Висота трикутника
        double pmx = (p1x + p2x) / 2;    // Середина по Х
        double pmy = (p1y + p2y) / 2;    // Середина по У
        double p3x = pmx + (h * (p1y - pmy)) / (a / 2);  // Координата Х верхньої точки трикутника
        double p3y = pmy + (h * (p1x - pmx)) / (a / 2);  // Координата У верхньої точки трикутника
        
        drawCurveKochRecur(new Line2D.Double(p1x,p1y,p2x,p2y), Iter); // Основа
        drawCurveKochRecur(new Line2D.Double(p3x,p3y,p1x,p1y), Iter); // Ліва сторона
        drawCurveKochRecur(new Line2D.Double(p2x,p2y,p3x,p3y), Iter); // Права сторона
}
   /**
    * 
    * @param line
    * @param maxIter - максимальна ітерація
    * @param curIter - поточна ітерація
    */
private void drawCurveKochRecur(Line2D line, int maxIter, int curIter) {
 
        if (curIter == maxIter) 
            drawLine(line);   // Малюєм лінію     
 
        if (curIter <= maxIter){ 
            
            double a = line.getP1().distance(line.getP2()); // знаходим дистанцію між точкою 1 та точкою 2
            a = a / 3;  // ділим лінію на 3
          
            double h = Math.sqrt(Math.pow(a, 2) - Math.pow((a / 2), 2) / 4); // знаходим висоту трикутника 
 
            Point2D ps = line.getP1();  // знаходим точку 1
            Point2D pe = line.getP2();  // знаходим точку 2
 
            Point2D pm = new Point2D.Double((ps.getX() + pe.getX()) / 2, (ps.getY() + pe.getY()) / 2); // Знаходим центр точок по Х та У
            Point2D p1 = new Point2D.Double((2 * ps.getX() + pe.getX()) / 3, (2 * ps.getY() + pe.getY()) / 3);  // Знаходим точку 1 нового трикутника 
            Point2D p2 = new Point2D.Double((2 * pe.getX() + ps.getX()) / 3, (2 * pe.getY() + ps.getY()) / 3);  // Знаходим точку 2 нового трикутника
            Point2D p3 = new Point2D.Double(
                    pm.getX() + (h * (-p2.getY() + pm.getY())) / (a / 2),
                    pm.getY() + (h * (p2.getX() - pm.getX())) / (a / 2)
                );  // Знаходим точку 3 нового трикутника
 
            // Рекурсія
            curIter++; // поточна ітерація +1
            drawCurveKochRecur(new Line2D.Double(ps,p1), maxIter, curIter);  // лінія з точки 1 нового трикутника в точку 1 основної лінії  __/\.__.
            drawCurveKochRecur(new Line2D.Double(p1,p3), maxIter, curIter);  // Малюєм лінію з точки 1 в точку 3   
            drawCurveKochRecur(new Line2D.Double(p3,p2), maxIter, curIter);  // малюєм лінію з точки 3 в точку 1
            drawCurveKochRecur(new Line2D.Double(p2,pe), maxIter, curIter);  // лінія з точки 2 нового трикутника в точку 2 основної лінії
 
        }
    }
 // Отримуємо потрібну кількість ітерацій
    private void drawCurveKochRecur(Line2D line, int maxIter) {
        drawCurveKochRecur(line, maxIter, 0); // передаємо кількість ітерацій та починаємо з 0
    }
 
    /* Намалювати лінію */
    public void drawLine(Line2D line) {
        g2.draw(new Line2D.Double(line.getP1(), line.getP2()));
    }    
 
    /**
     * @param args the command line arguments
     */
    //Створення вікна
    public static void main(String[] args) {
       JFrame f = new JFrame("Сніжинка Коха");
        f.addWindowListener(new WindowAdapter() {
 
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        JApplet applet = new SnowflakeKoch();
        f.getContentPane().add("Center", applet);
        applet.init();
        f.pack();
        f.setSize(new Dimension(800, 800));
        f.show();
    }
    
}
