package main;

import entity.*;
import object.*;

public class AssetSetter {
    GamePanel gp;

    int buffer = 0;
    public AssetSetter(GamePanel gp){
        this.gp = gp;
    }

    public void setPlayer(){
    }

    public void setObject(){
        gp.obj[0] = new OBJ_Boots(gp);
        gp.obj[0].worldX = 23 * gp.tileSize;
        gp.obj[0].worldY = 10 * gp.tileSize;

        gp.obj[1] = new OBJ_Lipoden(gp);
        gp.obj[1].worldX = 25 * gp.tileSize;
        gp.obj[1].worldY = 24 * gp.tileSize;

        gp.obj[2] = new OBJ_Lipoden(gp);
        gp.obj[2].worldX = 35 * gp.tileSize;
        gp.obj[2].worldY = 12 * gp.tileSize;
    }

    public void setNPC(){

        gp.npc[0] = new NPC_OldMan(gp);
        gp.npc[0].worldX = 31 * gp.tileSize;
        gp.npc[0].worldY = 15 * gp.tileSize;

        gp.npc[1] = new NPC_Sylvie(gp);
        gp.npc[1].worldX = 30 * gp.tileSize;
        gp.npc[1].worldY = 14 * gp.tileSize;

        gp.npc[2] = new NPC_Fort(gp);
        gp.npc[2].worldX = 29 * gp.tileSize;
        gp.npc[2].worldY = 13 * gp.tileSize;

        gp.npc[3] = new NPC_Amaryllis(gp);
        gp.npc[3].worldX = 28 * gp.tileSize;
        gp.npc[3].worldY = 12 * gp.tileSize;

    }

    public void setMonster(){
    }
}
