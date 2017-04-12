/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.dfki.chatView.defenceStrategy;

import de.dfki.eliza.chat.ChatManager;
import de.dfki.eliza.chat.decorators.ChatManagerDecorator;
import de.dfki.eliza.chat.decorators.DummyAssessmentDecorator;
import de.dfki.eliza.chat.decorators.assessments.ConspicuousConversationDecorator;
import de.dfki.eliza.chat.decorators.assessments.EmptyConversationDecorator;
import de.dfki.eliza.chat.decorators.assessments.NonConspicuousConversationDecorator;

/**
 *
 * @author Robbie
 */
public class ChatManagerDefenceStrategy {

    public static ChatManagerDecorator createChatManager(int itemIndex, ChatManager chatManager) {
        ChatManagerDecorator decoratedChatManager;
        if (itemIndex == 0) {
            DefenceStrategyConversationDecorator.DEFENCE_STRATEGY=0;
            decoratedChatManager = new DefenceStrategyConversationDecorator(chatManager);
        } else if (itemIndex == 1) {
            DefenceStrategyConversationDecorator.DEFENCE_STRATEGY=1;
            decoratedChatManager = new DefenceStrategyConversationDecorator(chatManager);
        } else if (itemIndex == 2) {
            DefenceStrategyConversationDecorator.DEFENCE_STRATEGY=2;
            decoratedChatManager = new DefenceStrategyConversationDecorator(chatManager);
        } else if (itemIndex == 3) {
            DefenceStrategyConversationDecorator.DEFENCE_STRATEGY=3;
            decoratedChatManager = new DefenceStrategyConversationDecorator(chatManager);
        } else if (itemIndex == 4) {
            DefenceStrategyConversationDecorator.DEFENCE_STRATEGY=4;
            decoratedChatManager = new DefenceStrategyConversationDecorator(chatManager);
        } else if (itemIndex == 5) {
            DefenceStrategyConversationDecorator.DEFENCE_STRATEGY=5;
            decoratedChatManager = new DefenceStrategyConversationDecorator(chatManager);
        } else {
            decoratedChatManager = new DummyAssessmentDecorator();
        }
        return decoratedChatManager;
    }

}
