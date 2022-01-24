package net.eugenpaul.jlexi.data.iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.eugenpaul.jlexi.component.text.structure.TextRow;

class PreOrderIteratorTest {

    // private Row rootGlyph;

    // /**
    //  * create following Tree:
    //  * 
    //  * <pre>
    //  *          root
    //  *       /   |     \
    //  *    a      b        c
    //  *  / | \   / \      /| \
    //  * 1  2  3  4  5   6  7  d
    //  *                       |
    //  *                       8
    //  * </pre>
    //  */
    // @BeforeEach
    // void init() {
    //     rootGlyph = new Row();

    //     // Generate branch A
    //     Row a = new Row();
    //     CharGlyph a1 = new CharGlyph('1', null);
    //     CharGlyph a2 = new CharGlyph('2', null);
    //     CharGlyph a3 = new CharGlyph('3', null);
    //     a.insert(a1, 0);
    //     a.insert(a2, 1);
    //     a.insert(a3, 2);

    //     rootGlyph.insert(a, 0);

    //     // Generate branch B
    //     Row b = new Row();
    //     CharGlyph b1 = new CharGlyph('4', null);
    //     CharGlyph b2 = new CharGlyph('5', null);
    //     b.insert(b1, 0);
    //     b.insert(b2, 1);

    //     rootGlyph.insert(b, 1);

    //     // Generate branch C
    //     Row c = new Row();

    //     Row cd = new Row();
    //     CharGlyph cd1 = new CharGlyph('8', null);
    //     cd.insert(cd1, 0);

    //     CharGlyph c1 = new CharGlyph('6', null);
    //     CharGlyph c2 = new CharGlyph('7', null);
    //     c.insert(c1, 0);
    //     c.insert(c2, 1);
    //     c.insert(cd, 2);

    //     rootGlyph.insert(c, 2);
    // }

    // @Test
    // void testIterator() {
    //     PreOrderLeefIterator iterator = new PreOrderLeefIterator(rootGlyph);

    //     iterator.first();

    //     assertTrue(iterator.hasNext());
    //     assertEquals('1', ((CharGlyph) iterator.next()).getC());
    //     assertEquals('2', ((CharGlyph) iterator.next()).getC());
    //     assertEquals('3', ((CharGlyph) iterator.next()).getC());
    //     assertEquals('4', ((CharGlyph) iterator.next()).getC());
    //     assertEquals('5', ((CharGlyph) iterator.next()).getC());
    //     assertEquals('6', ((CharGlyph) iterator.next()).getC());
    //     assertEquals('7', ((CharGlyph) iterator.next()).getC());
    //     assertEquals('8', ((CharGlyph) iterator.next()).getC());
    // }

}
