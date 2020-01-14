package websocket;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

import model.Message;
import com.google.gson.Gson;

public class MessageEncoder implements Encoder.Text<Message> {

    private static Gson gson = new Gson();

    @Override
    public String encode(Message message){
        String json = gson.toJson(message);
        return json;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {
    }
}