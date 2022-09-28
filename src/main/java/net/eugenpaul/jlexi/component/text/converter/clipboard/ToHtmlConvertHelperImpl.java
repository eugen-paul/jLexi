package net.eugenpaul.jlexi.component.text.converter.clipboard;

import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSDeclarationList;
import com.helger.css.decl.CSSExpression;

import net.eugenpaul.jlexi.component.text.format.element.TextElement;
import net.eugenpaul.jlexi.component.text.format.element.TextFormat;
import net.eugenpaul.jlexi.component.text.format.element.TextFormatEffect;
import net.eugenpaul.jlexi.resourcesmanager.textformat.params.FormatUnderlineType;

public class ToHtmlConvertHelperImpl implements ToHtmlConvertHelper {

    @Override
    public String toHtml(List<TextElement> text) {
        var doc = createDocument(text);
        return doc.outerHtml();
    }

    private Document createDocument(List<TextElement> text) {
        Document response = new Document("127.0.0.7");

        var body = response.body();

        TextFormat lastFormat = TextFormat.DEFAULT;
        TextFormatEffect lastEffect = TextFormatEffect.DEFAULT_FORMAT_EFFECT;

        Element currentParagraph = new Element("p");
        body.appendChild(currentParagraph);

        Element currentSpan = new Element("span");
        currentParagraph.appendChild(currentSpan);

        addStyle(lastFormat, lastEffect, false, currentSpan);
        StringBuilder currentText = new StringBuilder();

        for (var c : text) {
            if (!c.isEndOfLine()) {
                if (!c.getFormat().equals(lastFormat) || !c.getFormatEffect().equals(lastEffect)) {
                    currentSpan.text(currentText.toString());
                    lastFormat = c.getFormat();
                    lastEffect = c.getFormatEffect();

                    currentSpan = new Element("span");
                    currentParagraph.appendChild(currentSpan);

                    addStyle(lastFormat, lastEffect, false, currentSpan);

                    currentText = new StringBuilder();
                }

                currentText.append(c.toString());
            } else {
                boolean addSizeBreak = c.isEndOfSection();
                currentSpan.text(currentText.toString());

                currentParagraph = new Element("p");
                body.appendChild(currentParagraph);

                currentSpan = new Element("span");
                currentParagraph.appendChild(currentSpan);

                lastFormat = c.getFormat();
                lastEffect = c.getFormatEffect();

                addStyle(lastFormat, lastEffect, addSizeBreak, currentSpan);
                currentText = new StringBuilder();
            }
        }
        currentSpan.text(currentText.toString());

        return response;
    }

    private void addStyle(TextFormat lastFormat, TextFormatEffect lastEffect, boolean addSizeBreak,
            Element currentSpan) {
        var declaration = genDeclaration(lastFormat, lastEffect);

        if (addSizeBreak) {
            var sizeBreak = new CSSDeclaration(//
                    "page-break-before", //
                    CSSExpression.createSimple("always"));
            declaration.add(sizeBreak);
        }

        currentSpan.attr("style", declaration.getAsCSSString());
    }

    private CSSDeclarationList genDeclaration(TextFormat format, TextFormatEffect effect) {
        CSSDeclarationList response = new CSSDeclarationList();

        addFormatToDeclaration(response, format);
        addEffectToDeclaration(response, effect);

        return response;
    }

    private void addFormatToDeclaration(CSSDeclarationList declaration, TextFormat format) {
        var font = new CSSDeclaration("font-family", CSSExpression.createSimple(format.getFontName()));
        declaration.add(font);

        var size = new CSSDeclaration(//
                "font-size", //
                CSSExpression.createSimple(format.getFontsize() + "px"));
        declaration.add(size);

        var weight = new CSSDeclaration(//
                "font-weight", //
                CSSExpression.createSimple(//
                        format.getBold().booleanValue() ? "bold" : "normal"//
                ));
        declaration.add(weight);

        var style = new CSSDeclaration(//
                "font-style", //
                CSSExpression.createSimple(//
                        format.getItalic().booleanValue() ? "italic" : "normal"//
                ));
        declaration.add(style);

        var color = new CSSDeclaration(//
                "color", //
                CSSExpression.createSimple(HtmlColorHelper.toHexRGBColor(format.getFontColor()))//
        );
        declaration.add(color);

        var bgColor = new CSSDeclaration(//
                "background-color", //
                CSSExpression.createSimple(HtmlColorHelper.toHexRGBColor(format.getBackgroundColor()))//
        );
        declaration.add(bgColor);
    }

    private void addEffectToDeclaration(CSSDeclarationList declaration, TextFormatEffect effect) {
        if (effect.getUnderline() == FormatUnderlineType.SINGLE) {
            var ulStyle = "solid";
            var ulColor = HtmlColorHelper.toHexRGBColor(effect.getUnderlineColor());
            var expr = new CSSExpression();
            expr.addTermSimple(ulStyle);
            expr.addTermSimple(ulColor);
            var ul = new CSSDeclaration("border-bottom", expr);
            declaration.add(ul);
        } else if (effect.getUnderline() == FormatUnderlineType.DOUBLE) {
            var ulStyle = "double";
            var ulColor = HtmlColorHelper.toHexRGBColor(effect.getUnderlineColor());
            var expr = new CSSExpression();
            expr.addTermSimple(ulStyle);
            expr.addTermSimple(ulColor);
            var ul = new CSSDeclaration("border-bottom", expr);
            declaration.add(ul);
        }
    }

}
