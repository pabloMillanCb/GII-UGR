package civitas;

import java.util.ArrayList;
import java.util.Collections;

public class MazoSorpresas {
    private ArrayList<Sorpresa> sorpresas;
    private boolean barajada;
    private int usadas;
    private boolean debug;
    private ArrayList<Sorpresa> cartasEspeciales;
    private Sorpresa ultimaSorpresa;
    
    private void init(){
        sorpresas = new ArrayList<Sorpresa>();
        cartasEspeciales = new ArrayList<Sorpresa>();
        sorpresas.clear();
        cartasEspeciales.clear();
        barajada=false;
        usadas=0;
    }
    
    protected MazoSorpresas(boolean d){
        this.init();
        debug=d;
        Diario diario= Diario.getInstance();
        if(d)
            diario.ocurreEvento("Mazo debug activado.\n");
        else
            diario.ocurreEvento("Mazo debug desactivado.\n");    
    }
    
    protected MazoSorpresas(){
        this.init();
        debug=false;
    }
    
    protected void alMazo(Sorpresa s){
        if(!barajada)
            sorpresas.add(s);
    }
    
    protected Sorpresa siguiente(){
        if( (!barajada || usadas==sorpresas.size()) && !debug){
            Collections.shuffle(sorpresas);
            usadas=0;
            barajada=true;
        }
        usadas++;
        ultimaSorpresa=sorpresas.get(0);
        sorpresas.remove(0);
        sorpresas.add(ultimaSorpresa);
        
        return ultimaSorpresa;
    }
    
    protected void inhabilitarCartaEspecial(Sorpresa sorpresa){
        if(sorpresas.contains(sorpresa)){
            sorpresas.remove(sorpresa);
            cartasEspeciales.add(sorpresa);
            Diario.getInstance().ocurreEvento("Carta especial extraída.\n");
        }
    }
    
    protected void habilitarCartaEspecial(Sorpresa sorpresa){
        if(cartasEspeciales.contains(sorpresa)){
            cartasEspeciales.remove(sorpresa);
            sorpresas.add(sorpresa);
            Diario.getInstance().ocurreEvento("Carta especial rehabilitada.\n");
        }
    }
    
    protected String sorpresasToString(){
        String lista = sorpresas.toString();
        return lista;
    }
    
    protected String cartasEspecialesToString(){
        String lista = cartasEspeciales.toString();
        return lista;
    }
    
    protected String ultimaSorpresaToString(){
        String sorpresa = ultimaSorpresa.toString();
        return sorpresa;
    }
}
