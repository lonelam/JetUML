/*******************************************************************************
 * JetUML - A desktop application for fast UML diagramming.
 *
 * Copyright (C) 2016, 2017 by the contributors of the JetUML project.
 *
 * See: https://github.com/prmr/JetUML
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package ca.mcgill.cs.jetuml.graph;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import ca.mcgill.cs.jetuml.application.Clipboard;
import ca.mcgill.cs.jetuml.application.SelectionList;
import ca.mcgill.cs.jetuml.diagrams.ClassDiagramGraph;
import ca.mcgill.cs.jetuml.diagrams.StateDiagramGraph;
import ca.mcgill.cs.jetuml.geom.Point;
import ca.mcgill.cs.jetuml.geom.Rectangle;
import ca.mcgill.cs.jetuml.graph.edges.DependencyEdge;
import ca.mcgill.cs.jetuml.graph.edges.NoteEdge;
import ca.mcgill.cs.jetuml.graph.edges.StateTransitionEdge;
import ca.mcgill.cs.jetuml.graph.nodes.ClassNode;
import ca.mcgill.cs.jetuml.graph.nodes.FinalStateNode;
import ca.mcgill.cs.jetuml.graph.nodes.InitialStateNode;
import ca.mcgill.cs.jetuml.graph.nodes.NoteNode;
import ca.mcgill.cs.jetuml.graph.nodes.PackageNode;
import ca.mcgill.cs.jetuml.graph.nodes.PointNode;
import ca.mcgill.cs.jetuml.graph.nodes.StateNode;
import ca.mcgill.cs.jetuml.gui.GraphPanel;

/**
 * Tests various interactions normally triggered from the 
 * GUI. To the extent possible, use GUI-triggered methods
 * to create and manipulate the diagrams.
 */
public class TestUsageScenarios
{
	@Test
	public void testClassDiagramCopyClassNodesAndEdgesInsidePackageNode()
	{
		ClassDiagramGraph diagram = new ClassDiagramGraph();
		PackageNode p1 = new PackageNode();
		ClassNode c1 = new ClassNode();
		ClassNode c2 = new ClassNode();
		diagram.addNode(p1, new Point(20, 20));
		diagram.addNode(c1, new Point(25, 25));
		diagram.addNode(c2, new Point(30, 30));
		c2.translate(100, 0);
		DependencyEdge edge = new DependencyEdge();
		diagram.addEdge(edge, new Point(26, 26), new Point(131, 31));
		assertEquals(1, diagram.getRootNodes().size());
		assertEquals(2, p1.getChildren().size());
		assertEquals(c1, edge.getStart());
		assertEquals(c2, edge.getEnd());
		
		SelectionList selection = new SelectionList();
		selection.add(c1);
		selection.add(c2);
		selection.add(edge);
		
		Clipboard.instance().copy(selection);
		Clipboard.instance().paste(new GraphPanel(diagram, null));
		
		assertEquals(3, diagram.getRootNodes().size());
		assertEquals(2, diagram.getEdges().size());
	}
	
	@Test
	public void testClassDiagramCopyPackageNodesAndEdgesInsidePackageNode()
	{
		ClassDiagramGraph diagram = new ClassDiagramGraph();
		PackageNode p1 = new PackageNode();
		PackageNode c1 = new PackageNode();
		PackageNode c2 = new PackageNode();
		diagram.addNode(p1, new Point(20, 20));
		diagram.addNode(c1, new Point(30, 30));
		diagram.addNode(c2, new Point(25, 25));
		c2.translate(100, 0);
		DependencyEdge edge = new DependencyEdge();
		diagram.addEdge(edge, new Point(31, 31), new Point(130, 26));
		assertEquals(1, diagram.getRootNodes().size());
		assertEquals(2, p1.getChildren().size());
		assertEquals(1, diagram.getEdges().size());
		assertEquals(c1, edge.getStart());
		assertEquals(c2, edge.getEnd());
		
		SelectionList selection = new SelectionList();
		selection.add(p1);
		selection.add(edge);

		Clipboard.instance().copy(selection);
		Clipboard.instance().paste(new GraphPanel(diagram, null));
		
		assertEquals(2, diagram.getRootNodes().size());
		assertEquals(2, diagram.getEdges().size());
	}
	
	@Test
	public void testStateDiagramCreate()
	{
		// Create a state diagram with two state nodes, one start node, one end node
		StateDiagramGraph diagram = new StateDiagramGraph();
		StateNode node1 = new StateNode();
		node1.getName().setText("Node 1");
		StateNode node2 = new StateNode();
		node2.getName().setText("Node 2");
		InitialStateNode start = new InitialStateNode();
		FinalStateNode end = new FinalStateNode();
		diagram.addNode(node1, new Point(30,30));
		diagram.addNode(node2, new Point(30, 100));
		diagram.addNode(start, new Point(5, 5));
		diagram.addNode(end, new Point(30, 200));
		assertEquals(4, diagram.getRootNodes().size());
		
		// Add edges between all of these, including back-and-forth between two states. 
		StateTransitionEdge edge1 = new StateTransitionEdge();
		edge1.setMiddleLabel("Edge 1");
		diagram.addEdge(edge1, new Point(6, 6), new Point(35, 35));
		
		StateTransitionEdge edge2 = new StateTransitionEdge();
		edge2.setMiddleLabel("Edge 2");
		diagram.addEdge(edge2, new Point(35, 35), new Point(35, 105));
		
		StateTransitionEdge edge3 = new StateTransitionEdge();
		edge3.setMiddleLabel("Edge 3");
		diagram.addEdge(edge3, new Point(35, 105), new Point(35, 35));
		
		StateTransitionEdge edge4 = new StateTransitionEdge();
		edge4.setMiddleLabel("Edge 4");
		diagram.addEdge(edge4, new Point(35, 105), new Point(32, 202));
		
		NoteEdge noteEdge = new NoteEdge();
		diagram.addEdge(noteEdge, new Point(6, 6), new Point(35, 35));
		diagram.addEdge(noteEdge, new Point(35, 35), new Point(35, 105));
		diagram.addEdge(noteEdge, new Point(35, 105), new Point(35, 35));
		diagram.addEdge(noteEdge, new Point(35, 105), new Point(32, 202));
		
		// VALIDATION NODES
		assertEquals(4, diagram.getRootNodes().size());
		assertEquals(new Rectangle(30, 30, 80, 60), node1.view().getBounds());
		assertEquals("Node 1", node1.getName().getText());
		assertEquals(new Rectangle(30, 100, 80, 60), node2.view().getBounds());
		assertEquals("Node 2", node2.getName().getText());
		assertEquals(new Rectangle(5, 5, 20, 20), start.view().getBounds());
		assertEquals(new Rectangle(30, 200, 20, 20), end.view().getBounds());
		
		// VALIDATION EDGES
		assertEquals(4, diagram.getEdges().size());
		
		assertEquals(new Rectangle(11, 5, 38, 25), edge1.view().getBounds());
		assertEquals("Edge 1", edge1.getMiddleLabel());
		assertEquals(start, edge1.getStart());
		assertEquals(node1, edge1.getEnd());
		
		assertEquals(new Rectangle(53, 76, 38, 24), edge2.view().getBounds());
		assertEquals("Edge 2", edge2.getMiddleLabel());
		assertEquals(node1, edge2.getStart());
		assertEquals(node2, edge2.getEnd());
		
		assertEquals(new Rectangle(46, 76, 38, 24), edge3.view().getBounds());
		assertEquals("Edge 3", edge3.getMiddleLabel());
		assertEquals(node2, edge3.getStart());
		assertEquals(node1, edge3.getEnd());
		
		assertEquals(new Rectangle(44, 160, 51, 41), edge4.view().getBounds());
		assertEquals("Edge 4", edge4.getMiddleLabel());
		assertEquals(node2, edge4.getStart());
		assertEquals(end, edge4.getEnd());
	}
	
	@Test
	public void testStateDiagramCreateNotes() throws Exception
	{
		// Create a state diagram with two state nodes, one start node, one end node
		StateDiagramGraph diagram = new StateDiagramGraph();
		StateNode node1 = new StateNode();
		node1.getName().setText("Node 1");
		diagram.addNode(node1, new Point(30,30));
		NoteNode note = new NoteNode();
		diagram.addNode(note, new Point(130,130));
		assertEquals(2, diagram.getRootNodes().size());
		// Note edge with a point node not overlapping any nodes
		NoteEdge edge1 = new NoteEdge();
		diagram.addEdge(edge1, new Point(135,135), new Point(300,300));
		assertEquals(3, diagram.getRootNodes().size());
		assertTrue(diagram.getRootNodes().toArray(new Node[4])[2] instanceof PointNode);
		assertEquals(1, diagram.getEdges().size());
		
		// Note edge with a point node overlapping any nodes
		NoteEdge edge2 = new NoteEdge();
		diagram.addEdge(edge2, new Point(135,135), new Point(40,40));
		assertEquals(4, diagram.getRootNodes().size());
		assertTrue(diagram.getRootNodes().toArray(new Node[4])[3] instanceof PointNode);
		assertEquals(2, diagram.getEdges().size());
		
		// Note edge with a starting point on a node
		NoteEdge edge3 = new NoteEdge();
		diagram.addEdge(edge3, new Point(35,35), new Point(135,135));
		assertEquals(4, diagram.getRootNodes().size());
		assertTrue(diagram.getRootNodes().toArray(new Node[4])[3] instanceof PointNode);
		assertEquals(3, diagram.getEdges().size());
		assertEquals(node1, edge3.getStart());
		assertEquals(note, edge3.getEnd());
	}
}
