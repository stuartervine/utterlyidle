package com.googlecode.utterlyidle;

import com.googlecode.totallylazy.Uri;
import com.googlecode.utterlyidle.annotations.HttpMethod;
import org.junit.Test;

import static com.googlecode.totallylazy.Uri.uri;
import static com.googlecode.utterlyidle.BasePath.basePath;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.RequestBuilder.post;
import static com.googlecode.utterlyidle.Requests.request;
import static com.googlecode.utterlyidle.ResourcePath.resourcePath;
import static com.googlecode.utterlyidle.ResourcePath.resourcePathOf;
import static junit.framework.Assert.assertEquals;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class MemoryRequestTest {
    @Test
    public void shouldBeReversibleToRawMessage() {
        assertThat(post("http://www.youtube.com/watch?v=606eK4abteQ")
                .accepting("text/html")
                .withForm("chups", "nah bru")
                .withForm("plinkton", "nom")
                .withHeader("Cookie", "size=diciptive")
                .withHeader("Referer", "http://google.com").
                        build().toString(),
                   is(
                           "POST http://www.youtube.com/watch?v=606eK4abteQ HTTP/1.1\r\n" +
                                   "Accept: text/html\r\n" +
                                   "Content-Type: application/x-www-form-urlencoded; charset=UTF-8\r\n" +
                                   "Cookie: size=diciptive\r\n" +
                                   "Referer: http://google.com\r\n" +
                                   "Content-Length: 26\r\n" +
                                   "\r\n" +
                                   "chups=nah+bru&plinkton=nom"
                   ));
    }

    @Test
    public void toStringCanBeCalledMultipleTimes() throws Exception {
        Request request = request("GET", uri("smoosh"), HeaderParameters.headerParameters(), "some input".getBytes());

        assertThat(request.toString(), containsString("some input"));
        assertThat(request.toString(), containsString("some input"));
    }

    @Test
    public void shouldNotHoldOnToOldQueryParametersAfterUrlIsChanged() {
        Request request = get("http://www.google.com?q=handies+that+look+like+gandhis").build();

        assertThat(Requests.query(request).getValue("q"), is("handies that look like gandhis"));

        request.uri(uri("http://www.google.com?q=cheeses+that+look+like+jesus"));
        
        assertThat(Requests.query(request).getValue("q"), is("cheeses that look like jesus"));
    }

    @Test
    public void shouldSupportRetrievingResourcePath() throws Exception {
        assertThat(resourcePathOf(get("http://www.myserver.com/foobar/spaz").build()), is(resourcePath("/foobar/spaz")));
    }

    @Test
    public void shouldSupportEquals(){
        assertEquals(get("http://www.google.com").build(), get("http://www.google.com").build());
    }

    private Request createRequestWith(Uri uri) {
        return new MemoryRequest(HttpMethod.GET, uri, HeaderParameters.headerParameters(), "foo".getBytes());
    }

}
