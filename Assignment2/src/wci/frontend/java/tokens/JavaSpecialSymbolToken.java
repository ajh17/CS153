package wci.frontend.java.tokens;

import wci.frontend.Source;
import wci.frontend.java.JavaToken;

public class JavaSpecialSymbolToken extends JavaToken {

    /**
     * Constructor.
     * @param source the source from where to fetch the token's characters.
     * @throws Exception if an error occurred.
     */
    public JavaSpecialSymbolToken(Source source)
            throws Exception
    {
        super(source);
    }

}
