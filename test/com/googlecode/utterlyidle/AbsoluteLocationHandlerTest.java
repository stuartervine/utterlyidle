package com.googlecode.utterlyidle;

import org.hamcrest.Matchers;
import org.junit.Test;

import static com.googlecode.utterlyidle.HttpHeaders.HOST;
import static com.googlecode.utterlyidle.HttpHeaders.LOCATION;
import static com.googlecode.utterlyidle.RequestBuilder.get;
import static com.googlecode.utterlyidle.Responses.seeOther;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AbsoluteLocationHandlerTest {
    
    @Test
    public void shouldAllowRedirectionOutsideOfBasePathForAbsolutePaths() throws Exception {
        assertLocationIsCorrectlyModified("/penfold/2/foo", "/penfold/rest", "http://mayhost:8080/penfold/2/foo");
    }

    @Test
    public void makesRelativeLocationsAbsolute() throws Exception {
        assertLocationIsCorrectlyModified("foo", "http://mayhost:8080/foo");
        assertLocationIsCorrectlyModified("foo/bar?a=b", "http://mayhost:8080/foo/bar?a=b");
        assertLocationIsCorrectlyModified("/bar/bob", "http://mayhost:8080/bar/bob");
    }

    @Test
    public void doesNotModifyAbsoluteLocations() throws Exception {
        assertLocationIsCorrectlyModified("http://mayhost:8080/bar/bob", "http://mayhost:8080/bar/bob");
    }

    private void assertLocationIsCorrectlyModified(final String originalLocation, final String finalLocation) throws Exception {
        assertLocationIsCorrectlyModified(originalLocation, "/", finalLocation);
    }

    private void assertLocationIsCorrectlyModified(String originalLocation, String basePath, String finalLocation) throws Exception {
        Response response = new AbsoluteLocationHandler(returnResponse(seeOther(originalLocation)), BasePath.basePath(basePath)).
                handle(get("").withHeader(HOST, "mayhost:8080").build());
        assertThat(response.header(LOCATION), is(finalLocation));
        assertThat(response.status(), Matchers.is(Status.SEE_OTHER));
    }

    private HttpHandler returnResponse(final Response response) {
        return new HttpHandler() {
            public Response handle(Request request) throws Exception {
                return response;
            }
        };
    }
}
