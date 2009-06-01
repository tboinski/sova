package org.eti.kask.sova.edges;

import java.awt.Color;
import java.awt.Graphics2D;

// <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
// #[regen=yes,id=DCE.B7C99D74-BF0F-4B05-D6B3-E7E54BDEE4F1]
// </editor-fold> 
public class Edge {

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.4DE59F15-5FAB-F831-3F19-A2420FCFFF5C]
    // </editor-fold> 
    protected Color strokeColor;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.835E3069-DF1F-39A6-EACF-05513325683C]
    // </editor-fold> 
    protected int strokeWidth;

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.34E7DCE2-36DC-D3E7-7EC8-8CFDF8D4E076]
    // </editor-fold> 
    public Edge () {
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.D1A2D681-CDE3-BA7B-6064-1260D774B6B5]
    // </editor-fold> 
    public Color getStrokeColor () {
        return strokeColor;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.209A98D5-AB30-1096-6C17-837EAC30CF81]
    // </editor-fold> 
    public void setStrokeColor (Color val) {
        this.strokeColor = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.749333FD-92CF-870A-34E7-165DFA7F9C96]
    // </editor-fold> 
    public int getStrokeWidth () {
        return strokeWidth;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,regenBody=yes,id=DCE.D416D75E-5B30-8AB3-1861-FA0D95C6A647]
    // </editor-fold> 
    public void setStrokeWidth (int val) {
        this.strokeWidth = val;
    }

    // <editor-fold defaultstate="collapsed" desc=" UML Marker "> 
    // #[regen=yes,id=DCE.7F2F96F8-B319-8EE3-7EDB-3C2A09BB1022]
    // </editor-fold> 
    public void renderShape (Graphics2D g) {
    }

}

