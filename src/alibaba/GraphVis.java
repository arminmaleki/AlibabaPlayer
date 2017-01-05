package alibaba;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.functors.ChainedTransformer;

import edu.uci.ics.jung.algorithms.layout.AbstractLayout;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout;
import edu.uci.ics.jung.algorithms.layout.SpringLayout2;
import edu.uci.ics.jung.graph.DirectedGraph;
import edu.uci.ics.jung.graph.DirectedSparseMultigraph;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.CrossoverScalingControl;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ScalingControl;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.DefaultVertexLabelRenderer;
import edu.uci.ics.jung.visualization.renderers.VertexLabelAsShapeRenderer;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class GraphVis {
	DirectedGraph<String, String> g;
	VisualizationViewer<String, String> vv;
	SpringLayout<String, String> layout;
	
	// We create our graph in here
	// The Layout<V, E> is parameterized by the vertex and edge types
	public void draw(){
		
		
		// Add some edges. From above we defined these to be of type String
		// Note that the default is for undirected edges.
		//g.addEdge("Edge-A", "1", "2"); // Note that Java 1.5 auto-boxes primitives
		//g.addEdge("Edge-B", "2", "3");
		// Let's see what we have. Note the nice output from the
		// SparseMultigraph<V,E> toString() method
		System.out.println("The graph g = " + g.toString());
		//g.addVertex(""+4);
		// sets the initial size of the space
	    
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		//vv.repaint();
		
		
	}
	//static class setlength implements SpringLayout.LengthFunction{}
	
	public GraphVis(Transformer<String,Integer> length) {
		// Graph<V, E> where V is the type of the vertices
		// and E is the type of the edges
		g = new DirectedSparseMultigraph<String, String>();
	//	g.
		// Add some vertices. From above we defined these to be type Integer.
	//	g.addVertex( ""+1);
	//	g.addVertex(""+2);
	//	g.addVertex(""+3);
	layout = new SpringLayout<String, String>(g) ;
	layout.setRepulsionRange(300);
	//((FRLayout)layout).setMaxIterations(1000);
		layout.setSize(new Dimension(280, 280));
		
		// sets the initial size of the space
	
		// The BasicVisualizationServer<V,E> is parameterized by the edge types
		 vv = new VisualizationViewer<String, String>(
				layout);
		vv.setBackground(Color.WHITE);
		vv.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<>());
		vv.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);
	//	vv.getRenderer().getVertexLabelRender
		vv.setPreferredSize(new Dimension(280, 280)); // Sets the viewing area
		//	vv.setBounds(100, 100, 100, 100);	
			// g.addVertex("this");// size
		final DefaultModalGraphMouse<String,Number> graphMouse = 
	            new DefaultModalGraphMouse<String,Number>();
		  vv.setGraphMouse(graphMouse);
	        vv.addKeyListener(graphMouse.getModeKeyListener());
		  graphMouse.setMode(ModalGraphMouse.Mode.PICKING);
		  VertexLabelAsShapeRenderer<String,String> vlasr = new VertexLabelAsShapeRenderer<String,String>(vv.getRenderContext());
		  vv.getRenderContext().setVertexLabelTransformer(
	        		// this chains together Transformers so that the html tags
	        		// are prepended to the toString method output
	        		new ChainedTransformer<String,String>(new Transformer[]{
	        		new ToStringLabeller<String>(),
	        		new Transformer<String,String>() {
						public String transform(String input) {
							return// "<html><center>Vertex<p>"+
									" "+input+" ";
						}}})); 
		  vv.getRenderContext().setVertexShapeTransformer(vlasr);
	        vv.getRenderContext().setVertexLabelRenderer(new DefaultVertexLabelRenderer(Color.black));
	        vv.getRenderer().setVertexLabelRenderer(vlasr);
	        final ScalingControl scaler = new CrossoverScalingControl();
	        scaler.scale(vv, 0.8f, vv.getCenter());
	        layout.setForceMultiplier(layout.getForceMultiplier()*0.1);
	        layout.setStretch(layout.getStretch()*1.25);
	        
		  alibaba.AlibabaPlayer.panel2.add(vv);
		//	 g.addVertex("this");
		//layout.setGraph(g);
	//	layout.initialize();
	//	vv.repaint();		// JLabel l=new JLabel("HELLO");
		  
		    
		   
	}
}
