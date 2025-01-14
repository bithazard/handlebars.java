package com.github.jknack.handlebars.helper;

import static com.github.jknack.handlebars.helper.StringHelpers.abbreviate;
import static com.github.jknack.handlebars.helper.StringHelpers.capitalize;
import static com.github.jknack.handlebars.helper.StringHelpers.capitalizeFirst;
import static com.github.jknack.handlebars.helper.StringHelpers.center;
import static com.github.jknack.handlebars.helper.StringHelpers.cut;
import static com.github.jknack.handlebars.helper.StringHelpers.defaultIfEmpty;
import static com.github.jknack.handlebars.helper.StringHelpers.ljust;
import static com.github.jknack.handlebars.helper.StringHelpers.lower;
import static com.github.jknack.handlebars.helper.StringHelpers.replace;
import static com.github.jknack.handlebars.helper.StringHelpers.rjust;
import static com.github.jknack.handlebars.helper.StringHelpers.slugify;
import static com.github.jknack.handlebars.helper.StringHelpers.stringFormat;
import static com.github.jknack.handlebars.helper.StringHelpers.stripTags;
import static com.github.jknack.handlebars.helper.StringHelpers.substring;
import static com.github.jknack.handlebars.helper.StringHelpers.upper;
import static com.github.jknack.handlebars.helper.StringHelpers.wordWrap;
import static com.github.jknack.handlebars.helper.StringHelpers.yesno;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.*;

import org.junit.Test;

import com.github.jknack.handlebars.AbstractTest;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.Helper;
import com.github.jknack.handlebars.Options;
import com.github.jknack.handlebars.TagType;
import com.github.jknack.handlebars.Template;

/**
 * Unit test for {@link StringHelpers}.
 *
 * @author edgar.espina
 * @since 0.2.2
 */
public class StringHelpersTest extends AbstractTest {

  @Test
  public void capFirst() throws IOException {
    Options options = mock(Options.class);
    when(options.isFalsy(any())).thenReturn(false);

    assertEquals("capitalizeFirst", capitalizeFirst.name());
    assertEquals("Handlebars.java",
        capitalizeFirst.apply("handlebars.java", options));

    verify(options).isFalsy(any());
  }

  @Test
  public void center() throws IOException {
    Options options = mock(Options.class);
    when(options.hash("size")).thenReturn(19);
    when(options.isFalsy(any())).thenReturn(false);
    when(options.hash("pad", " ")).thenReturn(null);

    assertEquals("center", center.name());
    assertEquals("  Handlebars.java  ",
        center.apply("Handlebars.java", options));

    verify(options).hash("size");
    verify(options).isFalsy(any());
    verify(options).hash("pad", " ");
  }

  @Test
  public void centerWithPad() throws IOException {
    Options options = mock(Options.class);
    when(options.hash("size")).thenReturn(19);
    when(options.hash("pad", " ")).thenReturn("*");
    when(options.isFalsy(any())).thenReturn(false);

    assertEquals("center", center.name());
    assertEquals("**Handlebars.java**",
        center.apply("Handlebars.java", options));

    verify(options).hash("size");
    verify(options).hash("pad", " ");
    verify(options).isFalsy(any());
  }

  @Test
  public void cut() throws IOException {
    Options options = mock(Options.class);
    when(options.param(0, " ")).thenReturn(" ");
    when(options.isFalsy(any())).thenReturn(false);

    assertEquals("cut", cut.name());
    assertEquals("handlebars.java",
        cut.apply("handle bars .  java", options));

    verify(options).param(0, " ");
    verify(options).isFalsy(any());
  }

  @Test
  public void cutNoWhitespace() throws IOException {
    Options options = mock(Options.class);
    when(options.param(0, " ")).thenReturn("*");
    when(options.isFalsy(any())).thenReturn(false);

    assertEquals("cut", cut.name());
    assertEquals("handlebars.java",
        cut.apply("handle*bars*.**java", options));

    verify(options).param(0, " ");
    verify(options).isFalsy(any());
  }

  @Test
  public void defaultStr() throws IOException {
    Options options = mock(Options.class);
    when(options.param(0, "")).thenReturn("handlebars.java");

    assertEquals("defaultIfEmpty", defaultIfEmpty.name());
    assertEquals("handlebars.java",
        defaultIfEmpty.apply(null, options));
    assertEquals("handlebars.java",
        defaultIfEmpty.apply(false, options));
    assertEquals("handlebars.java",
        defaultIfEmpty.apply(Collections.emptyList(), options));

    assertEquals("something",
        defaultIfEmpty.apply("something", options));

    verify(options, times(3)).param(anyInt(), anyString());
  }

  @Test
  public void joinIterable() throws IOException {
    shouldCompileTo("{{{join this \", \"}}}", Arrays.asList("6", "7", "8"),
            $("join", StringHelpers.join), "6, 7, 8");
  }

  @Test
  public void joinEmptyList() throws IOException {
    shouldCompileTo("{{{join this \", \"}}}", Collections.emptyList(),
            $("join", StringHelpers.join), "");
  }

  @Test
  public void joinIterator() throws IOException {
    shouldCompileTo("{{{join this \", \"}}}", Arrays.asList("6", "7", "8").iterator(),
        $("join", StringHelpers.join), "6, 7, 8");
  }

  @Test
  public void joinArray() throws IOException {
    shouldCompileTo("{{{join this \", \"}}}", new String[]{"6", "7", "8" },
        $("join", StringHelpers.join), "6, 7, 8");
  }

  @Test
  public void joinValues() throws IOException {
    shouldCompileTo("{{{join \"6\" 7 n8 \"-\"}}}", $("n8", 8), $("join", StringHelpers.join),
        "6-7-8");
  }

  @Test
  public void joinWithPrefixAndSuffix() throws IOException {
    shouldCompileTo("{{{join this \", \" prefix='<' suffix='>'}}}", Arrays.asList("6", "7", "8"),
        $("join", StringHelpers.join), "<6, 7, 8>");
  }

  @Test
  public void ljust() throws IOException {
    Options options = mock(Options.class);
    when(options.hash("size")).thenReturn(20);
    when(options.hash("pad", " ")).thenReturn(null);
    when(options.isFalsy(any())).thenReturn(false);

    assertEquals("ljust", ljust.name());
    assertEquals("Handlebars.java     ",
        ljust.apply("Handlebars.java", options));

    verify(options).hash("size");
    verify(options).hash("pad", " ");
    verify(options).isFalsy(any());
  }

  @Test
  public void ljustWithPad() throws IOException {
    Options options = mock(Options.class);
    when(options.hash("size")).thenReturn(17);
    when(options.hash("pad", " ")).thenReturn("+");
    when(options.isFalsy(any())).thenReturn(false);

    assertEquals("ljust", ljust.name());
    assertEquals("Handlebars.java++",
        ljust.apply("Handlebars.java", options));

    verify(options).hash("size");
    verify(options).hash("pad", " ");
    verify(options).isFalsy(any());
  }

  @Test
  public void rjust() throws IOException {
    Options options = mock(Options.class);
    when(options.hash("size")).thenReturn(20);
    when(options.hash("pad", " ")).thenReturn(null);
    when(options.isFalsy(any())).thenReturn(false);

    assertEquals("rjust", rjust.name());
    assertEquals("     Handlebars.java",
        rjust.apply("Handlebars.java", options));

    verify(options).hash("size");
    verify(options).hash("pad", " ");
    verify(options).isFalsy(any());
  }

  @Test
  public void rjustWithPad() throws IOException {
    Options options = mock(Options.class);
    when(options.hash("size")).thenReturn(17);
    when(options.hash("pad", " ")).thenReturn("+");
    when(options.isFalsy(any())).thenReturn(false);

    assertEquals("rjust", rjust.name());
    assertEquals("++Handlebars.java",
        rjust.apply("Handlebars.java", options));

    verify(options).hash("size");
    verify(options).hash("pad", " ");
    verify(options).isFalsy(any());
  }

  @Test
  public void substringWithStart() throws IOException {
    Handlebars hbs = mock(Handlebars.class);
    Context ctx = mock(Context.class);
    Template fn = mock(Template.class);

    Options options = new Options.Builder(hbs, substring.name(), TagType.VAR, ctx, fn)
        .setParams(new Object[]{11 })
        .build();

    assertEquals("substring", substring.name());
    assertEquals("java",
        substring.apply("Handlebars.java", options));
  }

  @Test
  public void substringWithStartAndEnd() throws IOException {
    Handlebars hbs = mock(Handlebars.class);
    Context ctx = mock(Context.class);
    Template fn = mock(Template.class);

    Options options = new Options.Builder(hbs, substring.name(), TagType.VAR, ctx, fn)
        .setParams(new Object[]{0, 10 })
        .build();

    assertEquals("substring", substring.name());
    assertEquals("Handlebars",
        substring.apply("Handlebars.java", options));
  }

  @Test
  public void lower() throws IOException {
    Options options = mock(Options.class);
    when(options.isFalsy(any())).thenReturn(false);

    assertEquals("lower", lower.name());
    assertEquals("handlebars.java",
        lower.apply("Handlebars.java", options));

    verify(options).isFalsy(any());
  }

  @Test
  public void upper() throws IOException {
    Options options = mock(Options.class);
    when(options.isFalsy(any())).thenReturn(false);

    assertEquals("upper", upper.name());
    assertEquals("HANDLEBARS.JAVA",
        upper.apply("Handlebars.java", options));

    verify(options).isFalsy(any());
  }

  @Test
  public void slugify() throws IOException {
    Options options = mock(Options.class);
    when(options.isFalsy(any())).thenReturn(false);

    assertEquals("slugify", slugify.name());
    assertEquals("joel-is-a-slug",
        slugify.apply("  Joel is a slug  ", options));

    verify(options).isFalsy(any());
  }

  @Test
  public void replace() throws IOException {
    Handlebars hbs = mock(Handlebars.class);
    Context ctx = mock(Context.class);
    Template fn = mock(Template.class);

    Options options = new Options.Builder(hbs, replace.name(), TagType.VAR, ctx, fn)
        .setParams(new Object[]{"...", "rocks" })
        .build();

    assertEquals("replace", replace.name());
    assertEquals("Handlebars rocks",
        replace.apply("Handlebars ...", options));
  }

  @Test
  public void stringFormat() throws IOException {
    Handlebars hbs = mock(Handlebars.class);
    Context ctx = mock(Context.class);
    Template fn = mock(Template.class);

    Options options = new Options.Builder(hbs, stringFormat.name(), TagType.VAR, ctx, fn)
        .setParams(new Object[]{"handlebars.java" })
        .build();

    assertEquals("stringFormat", stringFormat.name());

    assertEquals("Hello handlebars.java!",
        stringFormat.apply("Hello %s!", options));
  }

  @Test
  public void stringDecimalFormat() throws IOException {
    Handlebars hbs = mock(Handlebars.class);
    Context ctx = mock(Context.class);
    Template fn = mock(Template.class);

    Options options = new Options.Builder(hbs, stringFormat.name(), TagType.VAR, ctx, fn)
        .setParams(new Object[]{10.0 / 3.0 })
        .build();

    assertEquals("stringFormat", stringFormat.name());

    assertEquals(String.format("10 / 3 = %.2f", 10.0 / 3.0),
        stringFormat.apply("10 / 3 = %.2f", options));
  }

  @Test
  public void stripTags() throws IOException {
    Options options = mock(Options.class);
    when(options.isFalsy(any())).thenReturn(false);

    assertEquals("stripTags", stripTags.name());
    assertEquals("Joel is a slug",
        stripTags.apply("<b>Joel</b> <button>is</button> a <span>slug</span>",
            options));

    verify(options).isFalsy(any());
  }

  @Test
  public void stripTagsMultiLine() throws IOException {
    Options options = mock(Options.class);
    when(options.isFalsy(any())).thenReturn(false);

    assertEquals("stripTags", stripTags.name());
    assertEquals("Joel\nis a slug",
        stripTags.apply(
            "<b>Joel</b>\n<button>is<\n/button> a <span>slug</span>",
            options));

    verify(options).isFalsy(any());
  }

  @Test
  public void capitalize() throws IOException {
    Options options = mock(Options.class);
    when(options.hash("fully", false)).thenReturn(false)
            .thenReturn(true);
    when(options.isFalsy(any())).thenReturn(false);

    assertEquals("capitalize", capitalize.name());

    assertEquals("Handlebars Java",
        capitalize.apply("handlebars java", options));

    assertEquals("Handlebars Java",
        capitalize.apply("HAndleBars JAVA", options));

    verify(options, times(2)).hash("fully", false);
    verify(options, times(2)).isFalsy(any());
  }

  @Test
  public void abbreviate() throws IOException {
    Options options = mock(Options.class);
    when(options.isFalsy(any())).thenReturn(false);
    when(options.param(0, null)).thenReturn(13);

    assertEquals("abbreviate", abbreviate.name());

    assertEquals("Handlebars...",
        abbreviate.apply("Handlebars.java", options));

    verify(options).isFalsy(any());
    verify(options).param(0, null);
  }

  @Test
  public void wordWrap() throws IOException {
    Options options = mock(Options.class);
    when(options.isFalsy(any())).thenReturn(false);
    when(options.param(0, null)).thenReturn(5);

    assertEquals("wordWrap", wordWrap.name());

    assertEquals("Joel" + System.lineSeparator() + "is a"
        + System.lineSeparator() + "slug",
        wordWrap.apply("Joel is a slug", options));

    verify(options).isFalsy(any());
    verify(options).param(0, null);
  }

  @Test
  public void yesno() throws IOException {
    Options options = mock(Options.class);
    when(options.hash("yes", "yes")).thenReturn("yes");
    when(options.hash("no", "no")).thenReturn("no");
    when(options.hash("maybe", "maybe")).thenReturn("maybe");

    assertEquals("yesno", yesno.name());

    assertEquals("yes", yesno.apply(true, options));
    assertEquals("no", yesno.apply(false, options));
    assertEquals("maybe", yesno.apply(null, options));

    verify(options).hash("yes", "yes");
    verify(options).hash("no", "no");
    verify(options).hash("maybe", "maybe");
  }

  @Test
  public void yesnoCustom() throws IOException {
    Options options = mock(Options.class);
    when(options.hash("yes", "yes")).thenReturn("yea");
    when(options.hash("no", "no")).thenReturn("nop");
    when(options.hash("maybe", "maybe")).thenReturn("whatever");

    assertEquals("yesno", yesno.name());

    assertEquals("yea", yesno.apply(true, options));
    assertEquals("nop", yesno.apply(false, options));
    assertEquals("whatever", yesno.apply(null, options));

    verify(options).hash("yes", "yes");
    verify(options).hash("no", "no");
    verify(options).hash("maybe", "maybe");
  }

  @Test
  public void nullContext() throws IOException {
    Set<Helper<Object>> helpers = new LinkedHashSet<>(Arrays.asList(StringHelpers.values()));
    helpers.remove(StringHelpers.join);
    helpers.remove(StringHelpers.yesno);
    helpers.remove(StringHelpers.defaultIfEmpty);

    Options options = mock(Options.class);
    when(options.isFalsy(any())).thenReturn(true);
    when(options.param(0, null)).thenReturn(null);

    for (Helper<Object> helper : helpers) {
      assertNull(helper.apply($, options));
    }

    verify(options, times(helpers.size() - 1)).isFalsy(any());
    verify(options, times(helpers.size())).param(0, null);
  }

  @Test
  public void nullContextWithDefault() throws IOException {
    Set<Helper<Object>> helpers = new LinkedHashSet<>(Arrays.asList(StringHelpers.values()));
    helpers.remove(StringHelpers.join);
    helpers.remove(StringHelpers.yesno);
    helpers.remove(StringHelpers.defaultIfEmpty);

    String nothing = "nothing";
    Options options = mock(Options.class);
    when(options.isFalsy(any())).thenReturn(true);
    when(options.param(0, null)).thenReturn(nothing);

    for (Helper<Object> helper : helpers) {
      assertEquals(nothing, helper.apply($, options));
    }

    verify(options, times(helpers.size() - 1)).isFalsy(any());
    verify(options, times(helpers.size())).param(0, null);
  }

  @Test
  public void nullContextWithNumber() throws IOException {
    Set<Helper<Object>> helpers = new LinkedHashSet<>(Arrays.asList(StringHelpers.values()));
    helpers.remove(StringHelpers.join);
    helpers.remove(StringHelpers.yesno);
    helpers.remove(StringHelpers.defaultIfEmpty);

    Object number = 32;
    Options options = mock(Options.class);
    when(options.isFalsy(any())).thenReturn(true);
    when(options.param(0, null)).thenReturn(number);

    for (Helper<Object> helper : helpers) {
      assertEquals(number.toString(), helper.apply($, options));
    }

    verify(options, times(helpers.size() - 1)).isFalsy(any());
    verify(options, times(helpers.size())).param(0, null);
  }
}
