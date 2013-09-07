package wci.frontend.java;
import wci.frontend.*;

public class JavaToken extends Token {
    /**
     * Constructor.
     * @param source the source from where to fetch the token's characters.
     * @throws Exception if an error occurred.
     */
    protected JavaToken(Source source)
            throws Exception
    {
        super(source);
    }
}
