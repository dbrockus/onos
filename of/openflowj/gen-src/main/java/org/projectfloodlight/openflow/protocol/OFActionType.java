// Copyright (c) 2008 The Board of Trustees of The Leland Stanford Junior University
// Copyright (c) 2011, 2012 Open Networking Foundation
// Copyright (c) 2012, 2013 Big Switch Networks, Inc.
// This library was generated by the LoxiGen Compiler.
// See the file LICENSE.txt which should have been included in the source distribution

// Automatically generated by LOXI from template const.java
// Do not modify

package org.projectfloodlight.openflow.protocol;

import org.projectfloodlight.openflow.protocol.*;
import org.projectfloodlight.openflow.protocol.action.*;
import org.projectfloodlight.openflow.protocol.actionid.*;
import org.projectfloodlight.openflow.protocol.bsntlv.*;
import org.projectfloodlight.openflow.protocol.errormsg.*;
import org.projectfloodlight.openflow.protocol.meterband.*;
import org.projectfloodlight.openflow.protocol.instruction.*;
import org.projectfloodlight.openflow.protocol.instructionid.*;
import org.projectfloodlight.openflow.protocol.match.*;
import org.projectfloodlight.openflow.protocol.oxm.*;
import org.projectfloodlight.openflow.protocol.queueprop.*;
import org.projectfloodlight.openflow.types.*;
import org.projectfloodlight.openflow.util.*;
import org.projectfloodlight.openflow.exceptions.*;

public enum OFActionType {
     OUTPUT,
     SET_VLAN_VID,
     SET_VLAN_PCP,
     STRIP_VLAN,
     SET_DL_SRC,
     SET_DL_DST,
     SET_NW_SRC,
     SET_NW_DST,
     SET_NW_TOS,
     SET_TP_SRC,
     SET_TP_DST,
     ENQUEUE,
     EXPERIMENTER,
     SET_NW_ECN,
     COPY_TTL_OUT,
     COPY_TTL_IN,
     SET_MPLS_LABEL,
     SET_MPLS_TC,
     SET_MPLS_TTL,
     DEC_MPLS_TTL,
     PUSH_VLAN,
     POP_VLAN,
     PUSH_MPLS,
     POP_MPLS,
     SET_QUEUE,
     GROUP,
     SET_NW_TTL,
     DEC_NW_TTL,
     SET_FIELD,
     PUSH_PBB,
     POP_PBB;
}