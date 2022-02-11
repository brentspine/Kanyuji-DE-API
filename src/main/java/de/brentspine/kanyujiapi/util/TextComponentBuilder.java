package de.brentspine.kanyujiapi.util;

import java.util.*;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ClickEvent.Action;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class TextComponentBuilder {

    private String default_String;
    private TextComponent textComponent = new TextComponent();
    private HashMap<String, TextComponent> action_components = new HashMap<String, TextComponent>();

    public TextComponentBuilder(String default_String) {
        this.default_String = default_String;
    }

    public TextComponentBuilder() {}

    public TextComponentBuilder setDefaultString(String default_String) {
        this.default_String = default_String;
        return this;
    }

    public TextComponentBuilder add(String subString) {
        this.textComponent.addExtra(subString);
        return this;
    }

    public TextComponentBuilder add(TextComponent component) {
        if(component != null) this.textComponent.addExtra(component);
        return this;
    }

    public TextComponentBuilder addClickAction(String toReplace, String replacement, String hover, String command) {
        TextComponent component = new TextComponent(replacement);
        component.setClickEvent(new ClickEvent(Action.RUN_COMMAND, command));
        component.setHoverEvent(new HoverEvent(net.md_5.bungee.api.chat.HoverEvent.Action.SHOW_TEXT, new Text(hover)));
        String identifier = UUID.randomUUID().toString().replace("-","").substring(0,10);
        this.default_String = this.default_String.replace(toReplace,identifier);
        this.action_components.put(identifier, component);
        return this;
    }

    public String getDefault() {
        return this.default_String;
    }

    public TextComponentBuilder rebuild() {
        this.textComponent = build();
        return this;
    }

    public boolean isIdentifier(String toCheck) {
        return this.action_components.containsKey(toCheck);
    }

    private List<String> prepareStrings(String default_String) {
        ArrayList<String> preparedList = new ArrayList<String>(Arrays.asList(this.default_String));
        this.action_components.keySet().forEach(i -> {
            if(this.default_String.contains(i)) {
                ArrayList<String> toIterate = new ArrayList<String>(preparedList);
                preparedList.clear();
                for(String listSplit : toIterate) {
                    String[] splitted = listSplit.split(i);
                    if(splitted.length > 1) for(int counter = 0; counter < splitted.length; counter++) preparedList.add(splitted[counter] + (((counter == splitted.length-1)) ? "" : i));
                    else preparedList.add(splitted[0] + ((this.default_String.replace(i, "").endsWith(splitted[0]) && this.default_String.endsWith(i)) ? i : ""));
                }
            }
        });
        return preparedList;
    }

    private void assemble(List<String> preparedStringList) {
        for(String toBuild : preparedStringList) {
            if(toBuild.length() > 10) {
                String identifier = toBuild.substring(toBuild.length()-10, toBuild.length());
                if(isIdentifier(identifier)) {
                    add(toBuild.replace(identifier, ""));
                    add(this.action_components.get(identifier));
                    continue;
                }
            }
            add(toBuild);
        }
    }

    public TextComponent build() {
        assert this.default_String != null;
        List<String> preparedList = prepareStrings(getDefault());
        assemble(preparedList);
        return this.textComponent;
    }

}
