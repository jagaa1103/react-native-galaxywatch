import { NativeModules, NativeEventEmitter } from 'react-native';

const { Galaxywatch } = NativeModules;
const onMessage = (callback)=> {
    const eventEmitter = new NativeEventEmitter(Galaxywatch);
    eventEmitter.addListener('fromGalaxyWatch', (event) => {
        // callback(event.sendEventFromGalaxyWatch) // "someValue"
        alert(event.sendEventFromGalaxyWatch);
    });
}
const GalaxyWatchModule = Object.assign(Galaxywatch, {onMessage});
export default GalaxyWatchModule;
