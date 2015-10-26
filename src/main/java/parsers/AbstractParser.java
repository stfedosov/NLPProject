package parsers;

import com.google.common.collect.Sets;
import constants.StringConstants;

import java.io.IOException;
import java.util.Collection;

/**
 * @author sfedosov
 */
public abstract class AbstractParser {

    protected static final Collection<String> allowedTags = Sets.newHashSet(StringConstants.ALLOWED_TAGS);

    public abstract void run(final String inputFilePath) throws IOException;

}
