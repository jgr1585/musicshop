/*
 * Musicshop API
 * No description provided (generated by Swagger Codegen https://github.com/swagger-api/swagger-codegen)
 *
 * OpenAPI spec version: 3.0.1
 * Contact: techsupport@musicshop.com
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 *
 * Swagger Codegen version: 3.0.34
 *
 * Do not edit the class manually.
 *
 */
import {ApiClient} from '../ApiClient';
import {SongDTOLengthDuration} from './SongDTOLengthDuration';

/**
 * The SongDTOLengthUnits model module.
 * @module model/SongDTOLengthUnits
 * @version 3.0.1
 */
export class SongDTOLengthUnits {
  /**
   * Constructs a new <code>SongDTOLengthUnits</code>.
   * @alias module:model/SongDTOLengthUnits
   * @class
   */
  constructor() {
  }

  /**
   * Constructs a <code>SongDTOLengthUnits</code> from a plain JavaScript object, optionally creating a new instance.
   * Copies all relevant properties from <code>data</code> to <code>obj</code> if supplied or a new instance if not.
   * @param {Object} data The plain JavaScript object bearing properties of interest.
   * @param {module:model/SongDTOLengthUnits} obj Optional instance to populate.
   * @return {module:model/SongDTOLengthUnits} The populated <code>SongDTOLengthUnits</code> instance.
   */
  static constructFromObject(data, obj) {
    if (data) {
      obj = obj || new SongDTOLengthUnits();
      if (data.hasOwnProperty('duration'))
        obj.duration = SongDTOLengthDuration.constructFromObject(data['duration']);
      if (data.hasOwnProperty('durationEstimated'))
        obj.durationEstimated = ApiClient.convertToType(data['durationEstimated'], 'Boolean');
      if (data.hasOwnProperty('dateBased'))
        obj.dateBased = ApiClient.convertToType(data['dateBased'], 'Boolean');
      if (data.hasOwnProperty('timeBased'))
        obj.timeBased = ApiClient.convertToType(data['timeBased'], 'Boolean');
    }
    return obj;
  }
}

/**
 * @member {module:model/SongDTOLengthDuration} duration
 */
SongDTOLengthUnits.prototype.duration = undefined;

/**
 * @member {Boolean} durationEstimated
 */
SongDTOLengthUnits.prototype.durationEstimated = undefined;

/**
 * @member {Boolean} dateBased
 */
SongDTOLengthUnits.prototype.dateBased = undefined;

/**
 * @member {Boolean} timeBased
 */
SongDTOLengthUnits.prototype.timeBased = undefined;

