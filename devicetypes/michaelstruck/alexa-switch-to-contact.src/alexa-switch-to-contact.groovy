/**
 *  Alexa Switch to Contact
 *
 * 
 *  Uses code from SmartThings
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 *
 */

metadata {
        definition (name: "Alexa Switch to Contact", namespace: "JohntGB", author: "Johnt") {
        capability "Switch"
        //' capability "Switch Level"
		capability "Actuator"	//included to give compatibility with ActionTiles
        capability "Sensor"		//included to give compatibility with ActionTiles
        
		capability "Contact Sensor"			//"open","closed"

		attribute "about", "string"
    }

	// simulator metadata
	simulator {
	}
	// Settings
    preferences{
    	//' input "setLevelOn", "bool", title: "setLevel Turns On Light", defaultValue:false
    }
	// UI tile definitions
	tiles(scale: 2) {
		multiAttributeTile(name: "switch", type: "lighting", width: 6, height: 4, canChangeIcon: true, canChangeBackground: true) {
			tileAttribute("device.switch", key: "PRIMARY_CONTROL") {
    			attributeState "off", label: '${name}', action: "switch.on", backgroundColor: "#ffffff",icon: "https://raw.githubusercontent.com/JohntGB/SmartThingsZZZ/master/Other-SmartApps/AlexaHelper/AH-Off.png", nextState: "turningOn"
		      	attributeState "on", label: '${name}', action: "switch.off", backgroundColor: "#79b821",icon: "https://raw.githubusercontent.com/JohntGB/SmartThingsZZZ/master/Other-SmartApps/AlexaHelper/AH-On.png",  nextState: "turningOff"
				attributeState "turningOff", label: '${name}', action: "switch.on",backgroundColor: "#ffffff", icon: "https://raw.githubusercontent.com/JohntGB/SmartThingsZZZ/master/Other-SmartApps/AlexaHelper/AH-Off.png",  nextState: "turningOn"
		      	attributeState "turningOn", label: '${name}', action: "switch.off",backgroundColor: "#79b821", icon: "https://raw.githubusercontent.com/JohntGB/SmartThingsZZZ/master/Other-SmartApps/AlexaHelper/AH-On.png", nextState: "turningOff"
        	}
			/*
        		tileAttribute("device.level", key: "SLIDER_CONTROL") {
            		attributeState "level", action:"switch level.setLevel"
        		}
        		tileAttribute("level", key: "SECONDARY_CONTROL") {
              		attributeState "level", label: 'Light dimmed to ${currentValue}%'
        		}    
				*/
		}
 
		
        standardTile("on", "device.switch", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", label:'onX', action:"switch.on", icon:"st.switches.light.on"
		}
		standardTile("off", "device.switch", inactiveLabel: false, decoration: "flat", width: 2, height: 2) {
			state "default", label:'offX', action:"switch.off", icon:"st.switches.light.off"
		}
		standardTile("contact", "device.contact", inactiveLabel: false, height:2, width:2, canChangeIcon: false) {
        	state "default", label:"contact\nnot used" //, backgroundColor: "#ffffff" 
            state "closed", label:'${name}', backgroundColor: "#ffffff", icon:"st.contact.contact.closed" 
            state "open", label:'${name}', backgroundColor: "#53a7c0", icon:"st.contact.contact.open" 
		}

        valueTile("aboutTxt", "device.about", inactiveLabel: false, decoration: "flat", width: 6, height:2) {
            state "default", label:'${currentValue}'
		}

/*        valueTile("lValue", "device.level", inactiveLabel: true, height:2, width:2, decoration: "flat") {  
			state "levelValue", label:'${currentValue}%', unit:""
        }
		*/
		main "switch"
		//' details(["switch","on","lValue","off","aboutTxt"])
		details(["switch","on","contact","off","aboutTxt"])

	}
}
def installed() {
	showVersion()	
}
def parse(String description) {
}

def on() {
	sendEvent(name: "switch", value: "on", isStateChange: true)
    log.info "Alexa Switch to Contact sent 'On' command"
	sendEvent(name: "contact", value: "open")	
	log.info "Contact 'open' command sent"
    showVersion()
}

def off() {
	sendEvent(name: "switch", value: "off", isStateChange: true)
    log.info "Alexa Switch to Contact sent 'Off' command"

	sendEvent(name: "contact", value: "closed")
	log.info "Contact 'closed' command sent"

    showVersion()
}

def setLevel(val){
    log.info "Alexa Switch to Contact set to $val"
    
    // make sure we don't drive switches past allowed values (command will hang device waiting for it to
    // execute. Never commes back)
    if (val < 0){
    	val = 0
    }
    
    if( val > 100){
    	val = 100
    }
    
    if (val == 0){ 
    	sendEvent(name:"level",value:val,isStateChange: true)
        if (setLevelOn && device.currentValue("switch") == "on") off()
    }
    else
    {
    	sendEvent(name:"level",value:val,isStateChange: true)
    	sendEvent(name:"switch.setLevel",value:val,isStateChange: true)
        if (setLevelOn && device.currentValue("switch") == "off") on()
    }
}
def showVersion(){
	def versionTxt = "${appName()}: ${versionNum()}\n"
    try {if (parent.getSwitchAbout()){versionTxt += parent.getSwitchAbout()}}
    catch(e){versionTxt +="Installed from the SmartThings IDE"}
	sendEvent (name: "about", value:versionTxt) 
}
def versionNum(){
	def txt = "1.0.0 (12/01/2019)"
}
def appName(){
	def txt="Alexa Switch to Contact"
}