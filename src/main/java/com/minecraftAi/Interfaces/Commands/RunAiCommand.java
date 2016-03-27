package com.minecraftAi.Interfaces.Commands;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityCommandBlock;
import net.minecraft.util.ChatComponentText;

/**
 * Created by Mickey on 26/03/2016.
 */
public class RunAiCommand extends AbstractCommand {

    @Override
    public String getName() {
        return "runAI";
    }

    @Override
    public String getCommandUsage(ICommandSender iCommandSender) {
        return "\\runAI: -train <action>, action: 'gatherwood'" ;
    }

    @Override
    public void ProcessPlayer(EntityPlayer player, String[] params) {
        if(params.length == 0) {
            player.addChatMessage(new ChatComponentText("No parameters"));
        }
        int i = 0;
        while(i < params.length){
            switch (params[i]){
                case "-train":
                    i++;
                    player.addChatMessage(new ChatComponentText("Training " + params[i]));
                    break;
                default:
                    player.addChatMessage(new ChatComponentText("Not a recognised command"));

            }
        }
    }

    @Override
    public void ProcessCommandBlock(TileEntityCommandBlock commandBlock, String[] params) {

    }

    @Override
    public void ProcessServerConsole(ICommandSender console, String[] params) {

    }
}
