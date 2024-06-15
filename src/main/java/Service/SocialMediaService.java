package Service;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;

import java.util.List;

public class SocialMediaService {

    private final AccountDAO accountDAO;
    private final MessageDAO messageDAO;

    public SocialMediaService(AccountDAO accountDAO, MessageDAO messageDAO) {
        this.accountDAO = accountDAO;
        this.messageDAO = messageDAO;
    }

    public Account register(Account account) {
        if (account.getUsername() != null && !account.getUsername().isEmpty() &&
            account.getPassword() != null && account.getPassword().length() >= 4 &&
            accountDAO.getAccountByUsername(account.getUsername()) == null) {
            return accountDAO.createAccount(account);
        }
        return null;
    }

    public Account login(Account account) {
        Account existingAccount = accountDAO.getAccountByUsername(account.getUsername());
        if (existingAccount != null && existingAccount.getPassword().equals(account.getPassword())) {
            return existingAccount;
        }
        return null;
    }

    public Message createMessage(Message message) {
        if (message.getMessage_text() != null && !message.getMessage_text().isEmpty() &&
            message.getMessage_text().length() <= 255 &&
            accountDAO.getAccountById(message.getPosted_by()) != null) {
            return messageDAO.createMessage(message);
        }
        return null;
    }

    public List<Message> getAllMessages() {
        return messageDAO.getAllMessages();
    }

    public Message getMessageById(int messageId) {
        return messageDAO.getMessageById(messageId);
    }

    public Message deleteMessage(int messageId) {
        return messageDAO.deleteMessage(messageId);
    }

    public Message updateMessage(int messageId, String newText) {
        if (newText != null && !newText.isEmpty() && newText.length() <= 255) {
            return messageDAO.updateMessage(messageId, newText);
        }
        return null;
    }

    public List<Message> getMessagesByUser(int accountId) {
        return messageDAO.getMessagesByUser(accountId);
    }
}
