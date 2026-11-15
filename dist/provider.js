/* Adapter boundary: no Pleos SDK calls, vehicle signals or sensors are connected. */
class LocalVehicleProvider {
  constructor(key='partsaver-v1'){this.key=key;this.warning=''}
  load(){try{const raw=localStorage.getItem(this.key);if(!raw)return PartSaver.seed();const s=JSON.parse(raw);if(s.version!==1||!s.vehicle||!Array.isArray(s.parts)||s.parts.length!==8||!Array.isArray(s.records))throw Error('invalid');return s}catch(e){this.warning='저장 데이터를 읽을 수 없어 시연 데이터로 시작합니다.';return PartSaver.seed()}}
  save(state){try{localStorage.setItem(this.key,JSON.stringify(state));return true}catch(e){this.warning='브라우저 저장 공간에 접근할 수 없습니다. 현재 화면에서만 유지됩니다.';return false}}
  getSnapshot(state){return {odometer:state.vehicle.odometer,source:'simulation',motion:'manual-demo',capturedAt:new Date().toISOString()}}
}
window.LocalVehicleProvider=LocalVehicleProvider;
