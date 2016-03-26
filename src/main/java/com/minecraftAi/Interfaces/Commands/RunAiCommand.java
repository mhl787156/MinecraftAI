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
        return "\\runAI: -train <action>\n" +
                "action: tree" ;
    }

    @Override
    public void ProcessPlayer(EntityPlayer player, String[] params) {
        player.addChatMessage(new ChatComponentText("WHY HELLO THERE!"));
    }

    @Override
    public void ProcessCommandBlock(TileEntityCommandBlock commandBlock, String[] params) {

    }

    @Override
    public void ProcessServerConsole(ICommandSender console, String[] params) {

    }
}
