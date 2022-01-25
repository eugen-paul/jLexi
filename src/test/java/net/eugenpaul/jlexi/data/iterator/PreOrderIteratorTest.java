package net.eugenpaul.jlexi.data.iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import net.eugenpaul.jlexi.component.Glyph;
import net.eugenpaul.jlexi.component.iterator.NullIterator;
import net.eugenpaul.jlexi.component.iterator.PreOrderLeafIterator;

class PreOrderIteratorTest {

    private static abstract class DataGlyph extends Glyph {
        protected DataGlyph(Glyph parent) {
            super(parent);
        }

        public abstract Character getData();
    }

    private Glyph rootGlyph;

    /**
     * create following Tree:
     * 
     * <pre>
     *          root
     *       /   |     \
     *    a      b        c
     *  / | \   / \      /| \
     * 1  2  3  4  5   6  7  d
     *                       |
     *                       8
     * </pre>
     */
    @BeforeEach
    void init() {
        // Generate branch A
        Glyph a1 = generateLeaf('1');
        Glyph a2 = generateLeaf('2');
        Glyph a3 = generateLeaf('3');
        Glyph aRoot = generateContainer(List.of(a1, a2, a3));

        // Generate branch B
        Glyph b1 = generateLeaf('4');
        Glyph b2 = generateLeaf('5');
        Glyph bRoot = generateContainer(List.of(b1, b2));

        // Generate branch C
        Glyph cd1 = generateLeaf('8');
        Glyph cdRoot = generateContainer(List.of(cd1));

        Glyph c1 = generateLeaf('6');
        Glyph c2 = generateLeaf('7');
        Glyph cRoot = generateContainer(List.of(c1, c2, cdRoot));

        rootGlyph = generateContainer(List.of(aRoot, bRoot, cRoot));
    }

    private Glyph generateLeaf(Character leafData) {
        DataGlyph leaf = mock(DataGlyph.class);
        when(leaf.iterator()).thenReturn(new NullIterator<>());
        when(leaf.getData()).thenReturn(leafData);
        return leaf;
    }

    private Glyph generateContainer(List<Glyph> children) {
        Glyph container = mock(Glyph.class);
        when(container.iterator()).thenReturn(children.iterator());
        return container;
    }

    @Test
    void testIterator() {
        PreOrderLeafIterator iterator = new PreOrderLeafIterator(rootGlyph);

        assertTrue(iterator.hasNext());
        assertEquals('1', ((DataGlyph) iterator.next()).getData());
        assertEquals('2', ((DataGlyph) iterator.next()).getData());
        assertEquals('3', ((DataGlyph) iterator.next()).getData());
        assertEquals('4', ((DataGlyph) iterator.next()).getData());
        assertEquals('5', ((DataGlyph) iterator.next()).getData());
        assertEquals('6', ((DataGlyph) iterator.next()).getData());
        assertEquals('7', ((DataGlyph) iterator.next()).getData());
        assertEquals('8', ((DataGlyph) iterator.next()).getData());
    }

}
