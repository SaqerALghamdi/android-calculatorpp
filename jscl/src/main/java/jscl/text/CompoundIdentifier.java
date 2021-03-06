package jscl.text;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import jscl.math.Generic;

public class CompoundIdentifier implements Parser<String> {

    public static final Parser<String> parser = new CompoundIdentifier();

    private CompoundIdentifier() {
    }

    @Nonnull
    public String parse(@Nonnull Parameters p, @Nullable Generic previousSumElement) throws ParseException {
        int pos0 = p.position.intValue();

        final StringBuilder result;

        ParserUtils.skipWhitespaces(p);
        try {
            final String identifier = Identifier.parser.parse(p, previousSumElement);
            result = new StringBuilder();
            result.append(identifier);
        } catch (ParseException e) {
            p.position.setValue(pos0);
            throw e;
        }

        while (true) {
            try {
                final String dotAndId = DotAndIdentifier.parser.parse(p, previousSumElement);
                // NOTE: '.' must be appended after parsing
                result.append(".").append(dotAndId);
            } catch (ParseException e) {
                p.exceptionsPool.release(e);
                break;
            }
        }

        return result.toString();
    }
}

class DotAndIdentifier implements Parser<String> {

    public static final Parser<String> parser = new DotAndIdentifier();

    private DotAndIdentifier() {
    }

    public String parse(@Nonnull Parameters p, Generic previousSumElement) throws ParseException {
        int pos0 = p.position.intValue();

        ParserUtils.tryToParse(p, pos0, '.');

        String result;
        try {
            result = Identifier.parser.parse(p, previousSumElement);
        } catch (ParseException e) {
            p.position.setValue(pos0);
            throw e;
        }

        return result;
    }
}
