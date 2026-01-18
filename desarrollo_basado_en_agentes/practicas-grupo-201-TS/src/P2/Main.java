/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package P2;


import P1.*;
import appboot.LARVABoot;
import static crypto.Keygen.getHexaKey;
    
/**
* @author albertoLlamasGonzalez
* @author pabloMillanCubero
* @author pabloBorregoMegías
* @author antonioFernandezCabrera
* @author franciscoAntonioPinoMuñoz
*/

public class Main {
    
    public static void main(String[] args) {

        LARVABoot boot = new LARVABoot();
        
        boot.Boot("isg2.ugr.es", 1099);
        boot.launchAgent("STF-"+getHexaKey(4), TS.class);
        
        boot.WaitToShutDown();
    }
}
