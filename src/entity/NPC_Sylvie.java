package entity;

import main.GamePanel;

public class NPC_Sylvie extends Entity {

    public NPC_Sylvie(GamePanel gp) {
        super(gp);

        type = 2;
        setName("Sylvie");

        this.solidArea.x = 8;
        this.solidArea.y = 16;
        this.solidAreaDefaultX = this.solidArea.x;
        this.solidAreaDefaultY = this.solidArea.y;
        this.solidArea.width = 32;
        this.solidArea.height = 32;

        getImage("sylvie");
        setDefaultValues(1, 100, 400,3, 7, 5, 19, 5,  9);
//        setDialogue();
    }

    public void setStatIncrements(){
        this.vit += 1;
        this.pow += 1;
        this.mag += 4;
        this.agi += 1;
    }

    public void setAction() {
        actionLockCounter++;

        if (actionLockCounter >= gp.randomize(120, 750)) {
//            System.out.println(getName() + " moves but has been defeated is " + isDefeated );
            int i = gp.randomize(1, 150);

            if (i <= 25) {
                direction = "up";
                isIdling = false;
            } else if (i <= 50) {
                direction = "down";
                isIdling = false;
            } else if (i <= 75) {
                direction = "left";
                isIdling = false;
            } else if (i <= 100) {
                direction = "right";
                isIdling = false;
            } else if (i <= 125) {
                isIdling = true;
            }
            actionLockCounter = 0;
        }
        spriteAnim(2);
    }

    public void setDialogue(){
        gp.event.dialogues.add("Hello, lad.");
//        gp.event.dialogues[1] = "You seem lost.";
//        gp.event.dialogues[2] = "I can fix that.";
//        gp.event.dialogues[3] = "No. Seriously, dude... \nI can.";
//        gp.event.dialogues[4] = "Why tf does no one believe what I \nsay?";
    }

    public void speak(){
        super.speak();
    }
}


