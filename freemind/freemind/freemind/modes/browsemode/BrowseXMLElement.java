/*FreeMind - A Program for creating and viewing Mindmaps
 *Copyright (C) 2000-2001  Joerg Mueller <joergmueller@bigfoot.com>
 *See COPYING for Details
 *
 *This program is free software; you can redistribute it and/or
 *modify it under the terms of the GNU General Public License
 *as published by the Free Software Foundation; either version 2
 *of the License, or (at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
/* $Id: BrowseXMLElement.java,v 1.6.18.2.2.2 2009/03/09 18:45:05 christianfoltin Exp $ */


package freemind.modes.browsemode;

import java.util.HashMap;
import java.util.Vector;

import freemind.main.FreeMindMain;
import freemind.main.XMLElement;
//#if defined(ARROW_LINK)
//@#$LPS-ARROW_LINK:GranularityType:Import
import freemind.modes.ArrowLinkAdapter;
//#endif
//#if defined(CLOUD)
//@#$LPS-CLOUD:GranularityType:Import
import freemind.modes.CloudAdapter;
//#endif
import freemind.modes.EdgeAdapter;
import freemind.modes.MindMap;
import freemind.modes.ModeController;
import freemind.modes.NodeAdapter;
import freemind.modes.XMLElementAdapter;

public class BrowseXMLElement extends XMLElementAdapter {

   private static final String ENCRYPTED_BROWSE_NODE = EncryptedBrowseNode.class.getName();
   private final ModeController mModeController;

   public BrowseXMLElement(ModeController pModeController) {
       super(pModeController);
       mModeController = pModeController;
   }

    //#if defined(ARROW_LINK)
    //@#$LPS-ARROW_LINK:GranularityType:Method
    protected BrowseXMLElement(ModeController pModeController, Vector ArrowLinkAdapters, HashMap IDToTarget) {
        super(pModeController, ArrowLinkAdapters, IDToTarget);
        mModeController = pModeController;
    }
    //#endif

    /** abstract method to create elements of my type (factory).*/
    protected XMLElement  createAnotherElement(){
    // We do not need to initialize the things of XMLElement.
        return new BrowseXMLElement(mModeController
        		//#if defined(ARROW_LINK)
        		//@#$LPS-ARROW_LINK:GranularityType:Statement
        		, mArrowLinkAdapters, mIDToTarget
        		//#endif
        		);
    }
    protected NodeAdapter createNodeAdapter(FreeMindMain     frame, String nodeClass){
    		if(nodeClass == ENCRYPTED_BROWSE_NODE){
    			return new EncryptedBrowseNode(frame, mModeController);
    		}
        return new BrowseNodeModel(frame, getMap());
    }
    protected EdgeAdapter createEdgeAdapter(NodeAdapter node, FreeMindMain frame){
        return new BrowseEdgeModel(node, frame);
    }
    //#if defined(CLOUD)
    //@#$LPS-CLOUD:GranularityType:Method
    protected CloudAdapter createCloudAdapter(NodeAdapter node, FreeMindMain frame){
        return new BrowseCloudModel(node, frame);
    }
    //#endif
    //#if defined(ARROW_LINK)
    //@#$LPS-ARROW_LINK:GranularityType:Method
    protected ArrowLinkAdapter createArrowLinkAdapter(NodeAdapter source, NodeAdapter target, FreeMindMain frame) {
        return new BrowseArrowLinkModel(source,target,frame);
    }
    //#endif

    protected NodeAdapter createEncryptedNode(String additionalInfo) {
		NodeAdapter node = createNodeAdapter(frame, ENCRYPTED_BROWSE_NODE);
		setUserObject(node);
        copyAttributesToNode(node);
	    node.setAdditionalInfo(additionalInfo);
        return node;
	}
}

