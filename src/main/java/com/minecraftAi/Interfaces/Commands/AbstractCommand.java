package com.minecraftAi.Interfaces.Commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntityCommandBlock;

/**
 * Created by Mickey on 26/03/2016.
 */
public abstract class AbstractCommand extends CommandBase {

    public abstract void ProcessPlayer(EntityPlayer player, String[] params);
    public abstract void ProcessCommandBlock(TileEntityCommandBlock commandBlock, String[] params);
    public abstract void ProcessServerConsole(ICommandSender console, String[] params);

    @Override
    public abstract String getName();

    @Override
    public abstract String getCommandUsage(ICommandSender iCommandSender);

    @Override
    public void execute(ICommandSender iCommandSender, String[] strings) throws CommandException{

        if(iCommandSender instanceof EntityPlayer) // If the sender is a player
        {
            ProcessPlayer((EntityPlayer) iCommandSender, strings);
            // Cast the sender into an EntityPlayer then call the ProcessPlayer method
        }
        else if(iCommandSender instanceof TileEntityCommandBlock) // If the sender is a commandblock
        {
            ProcessCommandBlock((TileEntityCommandBlock) iCommandSender, strings);
        }
        else // If it's the Server console
        {
            ProcessServerConsole(iCommandSender, strings);
        }

    }
}
