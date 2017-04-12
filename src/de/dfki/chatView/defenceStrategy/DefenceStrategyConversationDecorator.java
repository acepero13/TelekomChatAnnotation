/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.chatView.defenceStrategy;

import de.dfki.eliza.chat.ChatManager;
import de.dfki.eliza.chat.decorators.AbstractConversationDecorator;
import de.dfki.eliza.files.models.Conversation;
/**
 *
 * @author Robbie
 */
public class DefenceStrategyConversationDecorator extends AbstractConversationDecorator{
    
    public static int DEFENCE_STRATEGY = 0;

    //TODO: ResetPosition
    public DefenceStrategyConversationDecorator(ChatManager chatManager) {
        super(chatManager);
    }

    protected boolean isSelectedConditionFulfilled(Conversation conversation) {
        return conversation.getDefenseStrategy() == DEFENCE_STRATEGY;
    }
}
