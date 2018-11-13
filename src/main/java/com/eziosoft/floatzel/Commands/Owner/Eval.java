package com.eziosoft.floatzel.Commands.Owner;

import com.eziosoft.floatzel.Commands.FCommand;
import com.eziosoft.floatzel.Util.Error;
import com.jagrosh.jdautilities.command.CommandEvent;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

public class Eval extends FCommand {
    public Eval(){
        name = "eval";
        help = "runs provided statement";
        ownerCommand = true;
        category = owner;
    }

    @Override
    protected void cmdrun(CommandEvent event){
        String code = event.getArgs();
        if (code.length() < 0){
            event.getChannel().sendMessage("Error: invalid arguments!").queue();
            return;
        }
        // enable the scripting manager
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
        try{
            engine.eval("var imports = new JavaImporter(java.io, java.lang, java.util, com.eziosoft.floatzel, com.eziosoft.floatzel.Database);");
        } catch (ScriptException e){
            Error.Catch(e);
            return;
        }
        // now actually run this
        try {
            engine.put("command", this);
            engine.put("event", event);
            engine.put("client", event.getClient());
            engine.put("guild", event.getGuild());
            engine.put("channel", event.getTextChannel());
            engine.put("jda", event.getJDA());
            // now try to run it
            Object out = engine.eval(
                    "(function() {" +
                            "with (imports) {" +
                            code +
                            "}" +
                            "})();");
            if (out == null){
                event.getChannel().sendMessage("```Whatever you tried probably worked, unless you requested a object```").queue();
                return;
            } else {
                event.getChannel().sendMessage("```"+out.toString()+"```").queue();
                return;
            }
        } catch (ScriptException e){
            Error.Catch(e);
        } catch (Exception e){
            Error.Catch(e);
        }
    }
}
