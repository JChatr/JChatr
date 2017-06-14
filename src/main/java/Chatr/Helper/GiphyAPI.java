package Chatr.Helper;

import java.io.IOException;
import java.util.HashMap;

import Chatr.View.CurrentChat.GIFCell.GIFCellController;
import at.mukprojects.giphy4j.Giphy;
import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import at.mukprojects.giphy4j.dao.HttpRequestSender;
import at.mukprojects.giphy4j.dao.RequestSender;
import at.mukprojects.giphy4j.entity.search.SearchFeed;
import at.mukprojects.giphy4j.entity.search.SearchGiphy;
import at.mukprojects.giphy4j.entity.search.SearchRandom;
import at.mukprojects.giphy4j.exception.GiphyException;
import at.mukprojects.giphy4j.http.Request;
import at.mukprojects.giphy4j.http.Response;
import at.mukprojects.giphy4j.util.UrlUtil;

public class GiphyAPI extends Giphy{
    private static org.apache.logging.log4j.Logger log = LogManager.getLogger(GIFCellController.class);

    private static final String SearchEndpoint = "http://api.giphy.com/v1/gifs/search";
    private static final String IDEndpoint = "http://api.giphy.com/v1/gifs/";
    private static final String TranslateEndpoint = "http://api.giphy.com/v1/gifs/translate";
    private static final String RandomEndpoint = "http://api.giphy.com/v1/gifs/random";
    private static final String TrendingEndpoint = "http://api.giphy.com/v1/gifs/trending";

    private static final String SearchStickerEndpoint = "http://api.giphy.com/v1/stickers/search";
    private static final String TranslateStickerEndpoint = "http://api.giphy.com/v1/stickers/translate";
    private static final String RandomEndpointSticker = "http://api.giphy.com/v1/stickers/random";
    private static final String TrendingStickerEndpoint = "http://api.giphy.com/v1/stickers/trending";

    private String apiKey;
    private RequestSender sender;
    private Gson gson;

    public GiphyAPI(String apiKey) {
        super(apiKey);
        this.apiKey = apiKey;
    }

    public SearchFeed trend(int limit, int offset) throws GiphyException {
        SearchFeed feed = null;

        HashMap<String, String> params = new HashMap<String, String>();

        params.put("api_key", apiKey);
        if (limit > 100) {
            params.put("limit", "100");
        } else {
            params.put("limit", limit + "");
        }
        params.put("offset", offset + "");

        Request request = new Request(UrlUtil.buildUrlQuery(TrendingEndpoint, params));

        try {
            Response response = sender.sendRequest(request);
            feed = gson.fromJson(response.getBody(), SearchFeed.class);
        } catch (JsonSyntaxException | IOException e) {
            log.error(e.getMessage(), e);
            throw new GiphyException(e);
        }

        return feed;
    }
}
