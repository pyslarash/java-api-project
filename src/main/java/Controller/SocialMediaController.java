package Controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import DAO.AccountDAO;
import DAO.MessageDAO;
import Model.Account;
import Model.Message;
import Service.SocialMediaService;
import io.javalin.Javalin;
import io.javalin.http.Context;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    private final SocialMediaService socialMediaService;

    public SocialMediaController() {
        AccountDAO accountDAO = new AccountDAO() {
        };
        
        MessageDAO messageDAO = new MessageDAO() {
        };

        this.socialMediaService = new SocialMediaService(accountDAO, messageDAO);
    }

    public SocialMediaController(SocialMediaService socialMediaService) {
        this.socialMediaService = socialMediaService;
    }

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        
        app.post("/register", this::registerHandler);
        app.post("/login", this::loginHandler);
        app.post("/messages", this::createMessageHandler);
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/{message_id}", this::getMessageByIdHandler);
        app.delete("/messages/{message_id}", this::deleteMessageHandler);
        app.patch("/messages/{message_id}", this::updateMessageHandler);
        app.get("/accounts/{account_id}/messages", this::getMessagesByUserHandler);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerHandler(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account createdAccount = socialMediaService.register(account);
        if (createdAccount != null) {
            ctx.json(createdAccount);
        } else {
            ctx.status(400);
        }
    }

    private void loginHandler(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);
        Account loggedInAccount = socialMediaService.login(account);
        if (loggedInAccount != null) {
            ctx.json(loggedInAccount);
        } else {
            ctx.status(401);
        }
    }

    private void createMessageHandler(Context ctx) {
        Message message = ctx.bodyAsClass(Message.class);
        Message createdMessage = socialMediaService.createMessage(message);
        if (createdMessage != null) {
            ctx.json(createdMessage);
        } else {
            ctx.status(400);
        }
    }

    private void getAllMessagesHandler(Context ctx) {
        ctx.json(socialMediaService.getAllMessages());
    }

    private void getMessageByIdHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message message = socialMediaService.getMessageById(messageId);
        if (message != null) {
            ctx.json(message);
        } else {
            ctx.status(200).json("");
        }
    }

    private void deleteMessageHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        Message deletedMessage = socialMediaService.deleteMessage(messageId);
        if (deletedMessage != null) {
            ctx.json(deletedMessage);
        } else {
            ctx.status(200).json("");
        }
    }

    private void updateMessageHandler(Context ctx) {
        int messageId = Integer.parseInt(ctx.pathParam("message_id"));
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(ctx.body());
    
            String newText = jsonNode.get("message_text").asText();
    
            Message updatedMessage = socialMediaService.updateMessage(messageId, newText);
            
            if (updatedMessage != null) {
                ctx.json(updatedMessage).status(200);
            } else {
                ctx.status(400);
            }
        } catch (Exception e) {
            e.printStackTrace();
            ctx.status(500).result("Error processing request");
        }
    }

    private void getMessagesByUserHandler(Context ctx) {
        int accountId = Integer.parseInt(ctx.pathParam("account_id"));
        ctx.json(socialMediaService.getMessagesByUser(accountId));
    }


}