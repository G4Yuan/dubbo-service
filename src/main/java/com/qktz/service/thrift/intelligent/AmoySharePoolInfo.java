/**
 * Autogenerated by Thrift Compiler (0.9.0)
 *
 * DO NOT EDIT UNLESS YOU ARE SURE THAT YOU KNOW WHAT YOU ARE DOING
 *  @generated
 */
package com.qktz.service.thrift.intelligent;

import org.apache.thrift.scheme.IScheme;
import org.apache.thrift.scheme.SchemeFactory;
import org.apache.thrift.scheme.StandardScheme;

import org.apache.thrift.scheme.TupleScheme;
import org.apache.thrift.protocol.TTupleProtocol;
import org.apache.thrift.protocol.TProtocolException;
import org.apache.thrift.EncodingUtils;
import org.apache.thrift.TException;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.EnumMap;
import java.util.Set;
import java.util.HashSet;
import java.util.EnumSet;
import java.util.Collections;
import java.util.BitSet;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AmoySharePoolInfo implements org.apache.thrift.TBase<AmoySharePoolInfo, AmoySharePoolInfo._Fields>, java.io.Serializable, Cloneable {
  private static final org.apache.thrift.protocol.TStruct STRUCT_DESC = new org.apache.thrift.protocol.TStruct("AmoySharePoolInfo");

  private static final org.apache.thrift.protocol.TField ID_FIELD_DESC = new org.apache.thrift.protocol.TField("id", org.apache.thrift.protocol.TType.I32, (short)1);
  private static final org.apache.thrift.protocol.TField STOCK_TODAY_FIELD_DESC = new org.apache.thrift.protocol.TField("stock_today", org.apache.thrift.protocol.TType.LIST, (short)2);
  private static final org.apache.thrift.protocol.TField STOCK_NOTICE_FIELD_DESC = new org.apache.thrift.protocol.TField("stock_notice", org.apache.thrift.protocol.TType.LIST, (short)3);
  private static final org.apache.thrift.protocol.TField CREATOR_FIELD_DESC = new org.apache.thrift.protocol.TField("creator", org.apache.thrift.protocol.TType.I32, (short)4);
  private static final org.apache.thrift.protocol.TField CREATE_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("create_time", org.apache.thrift.protocol.TType.STRING, (short)5);
  private static final org.apache.thrift.protocol.TField MODIFIER_FIELD_DESC = new org.apache.thrift.protocol.TField("modifier", org.apache.thrift.protocol.TType.I32, (short)6);
  private static final org.apache.thrift.protocol.TField MODIFY_TIME_FIELD_DESC = new org.apache.thrift.protocol.TField("modify_time", org.apache.thrift.protocol.TType.STRING, (short)7);

  private static final Map<Class<? extends IScheme>, SchemeFactory> schemes = new HashMap<Class<? extends IScheme>, SchemeFactory>();
  static {
    schemes.put(StandardScheme.class, new AmoySharePoolInfoStandardSchemeFactory());
    schemes.put(TupleScheme.class, new AmoySharePoolInfoTupleSchemeFactory());
  }

  public int id; // required
  public List<StockInfo> stock_today; // required
  public List<StockInfo> stock_notice; // required
  public int creator; // required
  public String create_time; // required
  public int modifier; // required
  public String modify_time; // required

  /** The set of fields this struct contains, along with convenience methods for finding and manipulating them. */
  public enum _Fields implements org.apache.thrift.TFieldIdEnum {
    ID((short)1, "id"),
    STOCK_TODAY((short)2, "stock_today"),
    STOCK_NOTICE((short)3, "stock_notice"),
    CREATOR((short)4, "creator"),
    CREATE_TIME((short)5, "create_time"),
    MODIFIER((short)6, "modifier"),
    MODIFY_TIME((short)7, "modify_time");

    private static final Map<String, _Fields> byName = new HashMap<String, _Fields>();

    static {
      for (_Fields field : EnumSet.allOf(_Fields.class)) {
        byName.put(field.getFieldName(), field);
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, or null if its not found.
     */
    public static _Fields findByThriftId(int fieldId) {
      switch(fieldId) {
        case 1: // ID
          return ID;
        case 2: // STOCK_TODAY
          return STOCK_TODAY;
        case 3: // STOCK_NOTICE
          return STOCK_NOTICE;
        case 4: // CREATOR
          return CREATOR;
        case 5: // CREATE_TIME
          return CREATE_TIME;
        case 6: // MODIFIER
          return MODIFIER;
        case 7: // MODIFY_TIME
          return MODIFY_TIME;
        default:
          return null;
      }
    }

    /**
     * Find the _Fields constant that matches fieldId, throwing an exception
     * if it is not found.
     */
    public static _Fields findByThriftIdOrThrow(int fieldId) {
      _Fields fields = findByThriftId(fieldId);
      if (fields == null) throw new IllegalArgumentException("Field " + fieldId + " doesn't exist!");
      return fields;
    }

    /**
     * Find the _Fields constant that matches name, or null if its not found.
     */
    public static _Fields findByName(String name) {
      return byName.get(name);
    }

    private final short _thriftId;
    private final String _fieldName;

    _Fields(short thriftId, String fieldName) {
      _thriftId = thriftId;
      _fieldName = fieldName;
    }

    public short getThriftFieldId() {
      return _thriftId;
    }

    public String getFieldName() {
      return _fieldName;
    }
  }

  // isset id assignments
  private static final int __ID_ISSET_ID = 0;
  private static final int __CREATOR_ISSET_ID = 1;
  private static final int __MODIFIER_ISSET_ID = 2;
  private byte __isset_bitfield = 0;
  public static final Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> metaDataMap;
  static {
    Map<_Fields, org.apache.thrift.meta_data.FieldMetaData> tmpMap = new EnumMap<_Fields, org.apache.thrift.meta_data.FieldMetaData>(_Fields.class);
    tmpMap.put(_Fields.ID, new org.apache.thrift.meta_data.FieldMetaData("id", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32        , "int")));
    tmpMap.put(_Fields.STOCK_TODAY, new org.apache.thrift.meta_data.FieldMetaData("stock_today", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, StockInfo.class))));
    tmpMap.put(_Fields.STOCK_NOTICE, new org.apache.thrift.meta_data.FieldMetaData("stock_notice", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.ListMetaData(org.apache.thrift.protocol.TType.LIST, 
            new org.apache.thrift.meta_data.StructMetaData(org.apache.thrift.protocol.TType.STRUCT, StockInfo.class))));
    tmpMap.put(_Fields.CREATOR, new org.apache.thrift.meta_data.FieldMetaData("creator", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32        , "int")));
    tmpMap.put(_Fields.CREATE_TIME, new org.apache.thrift.meta_data.FieldMetaData("create_time", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    tmpMap.put(_Fields.MODIFIER, new org.apache.thrift.meta_data.FieldMetaData("modifier", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.I32        , "int")));
    tmpMap.put(_Fields.MODIFY_TIME, new org.apache.thrift.meta_data.FieldMetaData("modify_time", org.apache.thrift.TFieldRequirementType.DEFAULT, 
        new org.apache.thrift.meta_data.FieldValueMetaData(org.apache.thrift.protocol.TType.STRING)));
    metaDataMap = Collections.unmodifiableMap(tmpMap);
    org.apache.thrift.meta_data.FieldMetaData.addStructMetaDataMap(AmoySharePoolInfo.class, metaDataMap);
  }

  public AmoySharePoolInfo() {
  }

  public AmoySharePoolInfo(
    int id,
    List<StockInfo> stock_today,
    List<StockInfo> stock_notice,
    int creator,
    String create_time,
    int modifier,
    String modify_time)
  {
    this();
    this.id = id;
    setIdIsSet(true);
    this.stock_today = stock_today;
    this.stock_notice = stock_notice;
    this.creator = creator;
    setCreatorIsSet(true);
    this.create_time = create_time;
    this.modifier = modifier;
    setModifierIsSet(true);
    this.modify_time = modify_time;
  }

  /**
   * Performs a deep copy on <i>other</i>.
   */
  public AmoySharePoolInfo(AmoySharePoolInfo other) {
    __isset_bitfield = other.__isset_bitfield;
    this.id = other.id;
    if (other.isSetStock_today()) {
      List<StockInfo> __this__stock_today = new ArrayList<StockInfo>();
      for (StockInfo other_element : other.stock_today) {
        __this__stock_today.add(new StockInfo(other_element));
      }
      this.stock_today = __this__stock_today;
    }
    if (other.isSetStock_notice()) {
      List<StockInfo> __this__stock_notice = new ArrayList<StockInfo>();
      for (StockInfo other_element : other.stock_notice) {
        __this__stock_notice.add(new StockInfo(other_element));
      }
      this.stock_notice = __this__stock_notice;
    }
    this.creator = other.creator;
    if (other.isSetCreate_time()) {
      this.create_time = other.create_time;
    }
    this.modifier = other.modifier;
    if (other.isSetModify_time()) {
      this.modify_time = other.modify_time;
    }
  }

  public AmoySharePoolInfo deepCopy() {
    return new AmoySharePoolInfo(this);
  }

  @Override
  public void clear() {
    setIdIsSet(false);
    this.id = 0;
    this.stock_today = null;
    this.stock_notice = null;
    setCreatorIsSet(false);
    this.creator = 0;
    this.create_time = null;
    setModifierIsSet(false);
    this.modifier = 0;
    this.modify_time = null;
  }

  public int getId() {
    return this.id;
  }

  public AmoySharePoolInfo setId(int id) {
    this.id = id;
    setIdIsSet(true);
    return this;
  }

  public void unsetId() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __ID_ISSET_ID);
  }

  /** Returns true if field id is set (has been assigned a value) and false otherwise */
  public boolean isSetId() {
    return EncodingUtils.testBit(__isset_bitfield, __ID_ISSET_ID);
  }

  public void setIdIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __ID_ISSET_ID, value);
  }

  public int getStock_todaySize() {
    return (this.stock_today == null) ? 0 : this.stock_today.size();
  }

  public java.util.Iterator<StockInfo> getStock_todayIterator() {
    return (this.stock_today == null) ? null : this.stock_today.iterator();
  }

  public void addToStock_today(StockInfo elem) {
    if (this.stock_today == null) {
      this.stock_today = new ArrayList<StockInfo>();
    }
    this.stock_today.add(elem);
  }

  public List<StockInfo> getStock_today() {
    return this.stock_today;
  }

  public AmoySharePoolInfo setStock_today(List<StockInfo> stock_today) {
    this.stock_today = stock_today;
    return this;
  }

  public void unsetStock_today() {
    this.stock_today = null;
  }

  /** Returns true if field stock_today is set (has been assigned a value) and false otherwise */
  public boolean isSetStock_today() {
    return this.stock_today != null;
  }

  public void setStock_todayIsSet(boolean value) {
    if (!value) {
      this.stock_today = null;
    }
  }

  public int getStock_noticeSize() {
    return (this.stock_notice == null) ? 0 : this.stock_notice.size();
  }

  public java.util.Iterator<StockInfo> getStock_noticeIterator() {
    return (this.stock_notice == null) ? null : this.stock_notice.iterator();
  }

  public void addToStock_notice(StockInfo elem) {
    if (this.stock_notice == null) {
      this.stock_notice = new ArrayList<StockInfo>();
    }
    this.stock_notice.add(elem);
  }

  public List<StockInfo> getStock_notice() {
    return this.stock_notice;
  }

  public AmoySharePoolInfo setStock_notice(List<StockInfo> stock_notice) {
    this.stock_notice = stock_notice;
    return this;
  }

  public void unsetStock_notice() {
    this.stock_notice = null;
  }

  /** Returns true if field stock_notice is set (has been assigned a value) and false otherwise */
  public boolean isSetStock_notice() {
    return this.stock_notice != null;
  }

  public void setStock_noticeIsSet(boolean value) {
    if (!value) {
      this.stock_notice = null;
    }
  }

  public int getCreator() {
    return this.creator;
  }

  public AmoySharePoolInfo setCreator(int creator) {
    this.creator = creator;
    setCreatorIsSet(true);
    return this;
  }

  public void unsetCreator() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __CREATOR_ISSET_ID);
  }

  /** Returns true if field creator is set (has been assigned a value) and false otherwise */
  public boolean isSetCreator() {
    return EncodingUtils.testBit(__isset_bitfield, __CREATOR_ISSET_ID);
  }

  public void setCreatorIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __CREATOR_ISSET_ID, value);
  }

  public String getCreate_time() {
    return this.create_time;
  }

  public AmoySharePoolInfo setCreate_time(String create_time) {
    this.create_time = create_time;
    return this;
  }

  public void unsetCreate_time() {
    this.create_time = null;
  }

  /** Returns true if field create_time is set (has been assigned a value) and false otherwise */
  public boolean isSetCreate_time() {
    return this.create_time != null;
  }

  public void setCreate_timeIsSet(boolean value) {
    if (!value) {
      this.create_time = null;
    }
  }

  public int getModifier() {
    return this.modifier;
  }

  public AmoySharePoolInfo setModifier(int modifier) {
    this.modifier = modifier;
    setModifierIsSet(true);
    return this;
  }

  public void unsetModifier() {
    __isset_bitfield = EncodingUtils.clearBit(__isset_bitfield, __MODIFIER_ISSET_ID);
  }

  /** Returns true if field modifier is set (has been assigned a value) and false otherwise */
  public boolean isSetModifier() {
    return EncodingUtils.testBit(__isset_bitfield, __MODIFIER_ISSET_ID);
  }

  public void setModifierIsSet(boolean value) {
    __isset_bitfield = EncodingUtils.setBit(__isset_bitfield, __MODIFIER_ISSET_ID, value);
  }

  public String getModify_time() {
    return this.modify_time;
  }

  public AmoySharePoolInfo setModify_time(String modify_time) {
    this.modify_time = modify_time;
    return this;
  }

  public void unsetModify_time() {
    this.modify_time = null;
  }

  /** Returns true if field modify_time is set (has been assigned a value) and false otherwise */
  public boolean isSetModify_time() {
    return this.modify_time != null;
  }

  public void setModify_timeIsSet(boolean value) {
    if (!value) {
      this.modify_time = null;
    }
  }

  public void setFieldValue(_Fields field, Object value) {
    switch (field) {
    case ID:
      if (value == null) {
        unsetId();
      } else {
        setId((Integer)value);
      }
      break;

    case STOCK_TODAY:
      if (value == null) {
        unsetStock_today();
      } else {
        setStock_today((List<StockInfo>)value);
      }
      break;

    case STOCK_NOTICE:
      if (value == null) {
        unsetStock_notice();
      } else {
        setStock_notice((List<StockInfo>)value);
      }
      break;

    case CREATOR:
      if (value == null) {
        unsetCreator();
      } else {
        setCreator((Integer)value);
      }
      break;

    case CREATE_TIME:
      if (value == null) {
        unsetCreate_time();
      } else {
        setCreate_time((String)value);
      }
      break;

    case MODIFIER:
      if (value == null) {
        unsetModifier();
      } else {
        setModifier((Integer)value);
      }
      break;

    case MODIFY_TIME:
      if (value == null) {
        unsetModify_time();
      } else {
        setModify_time((String)value);
      }
      break;

    }
  }

  public Object getFieldValue(_Fields field) {
    switch (field) {
    case ID:
      return Integer.valueOf(getId());

    case STOCK_TODAY:
      return getStock_today();

    case STOCK_NOTICE:
      return getStock_notice();

    case CREATOR:
      return Integer.valueOf(getCreator());

    case CREATE_TIME:
      return getCreate_time();

    case MODIFIER:
      return Integer.valueOf(getModifier());

    case MODIFY_TIME:
      return getModify_time();

    }
    throw new IllegalStateException();
  }

  /** Returns true if field corresponding to fieldID is set (has been assigned a value) and false otherwise */
  public boolean isSet(_Fields field) {
    if (field == null) {
      throw new IllegalArgumentException();
    }

    switch (field) {
    case ID:
      return isSetId();
    case STOCK_TODAY:
      return isSetStock_today();
    case STOCK_NOTICE:
      return isSetStock_notice();
    case CREATOR:
      return isSetCreator();
    case CREATE_TIME:
      return isSetCreate_time();
    case MODIFIER:
      return isSetModifier();
    case MODIFY_TIME:
      return isSetModify_time();
    }
    throw new IllegalStateException();
  }

  @Override
  public boolean equals(Object that) {
    if (that == null)
      return false;
    if (that instanceof AmoySharePoolInfo)
      return this.equals((AmoySharePoolInfo)that);
    return false;
  }

  public boolean equals(AmoySharePoolInfo that) {
    if (that == null)
      return false;

    boolean this_present_id = true;
    boolean that_present_id = true;
    if (this_present_id || that_present_id) {
      if (!(this_present_id && that_present_id))
        return false;
      if (this.id != that.id)
        return false;
    }

    boolean this_present_stock_today = true && this.isSetStock_today();
    boolean that_present_stock_today = true && that.isSetStock_today();
    if (this_present_stock_today || that_present_stock_today) {
      if (!(this_present_stock_today && that_present_stock_today))
        return false;
      if (!this.stock_today.equals(that.stock_today))
        return false;
    }

    boolean this_present_stock_notice = true && this.isSetStock_notice();
    boolean that_present_stock_notice = true && that.isSetStock_notice();
    if (this_present_stock_notice || that_present_stock_notice) {
      if (!(this_present_stock_notice && that_present_stock_notice))
        return false;
      if (!this.stock_notice.equals(that.stock_notice))
        return false;
    }

    boolean this_present_creator = true;
    boolean that_present_creator = true;
    if (this_present_creator || that_present_creator) {
      if (!(this_present_creator && that_present_creator))
        return false;
      if (this.creator != that.creator)
        return false;
    }

    boolean this_present_create_time = true && this.isSetCreate_time();
    boolean that_present_create_time = true && that.isSetCreate_time();
    if (this_present_create_time || that_present_create_time) {
      if (!(this_present_create_time && that_present_create_time))
        return false;
      if (!this.create_time.equals(that.create_time))
        return false;
    }

    boolean this_present_modifier = true;
    boolean that_present_modifier = true;
    if (this_present_modifier || that_present_modifier) {
      if (!(this_present_modifier && that_present_modifier))
        return false;
      if (this.modifier != that.modifier)
        return false;
    }

    boolean this_present_modify_time = true && this.isSetModify_time();
    boolean that_present_modify_time = true && that.isSetModify_time();
    if (this_present_modify_time || that_present_modify_time) {
      if (!(this_present_modify_time && that_present_modify_time))
        return false;
      if (!this.modify_time.equals(that.modify_time))
        return false;
    }

    return true;
  }

  @Override
  public int hashCode() {
    return 0;
  }

  public int compareTo(AmoySharePoolInfo other) {
    if (!getClass().equals(other.getClass())) {
      return getClass().getName().compareTo(other.getClass().getName());
    }

    int lastComparison = 0;
    AmoySharePoolInfo typedOther = (AmoySharePoolInfo)other;

    lastComparison = Boolean.valueOf(isSetId()).compareTo(typedOther.isSetId());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetId()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.id, typedOther.id);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetStock_today()).compareTo(typedOther.isSetStock_today());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStock_today()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.stock_today, typedOther.stock_today);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetStock_notice()).compareTo(typedOther.isSetStock_notice());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetStock_notice()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.stock_notice, typedOther.stock_notice);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCreator()).compareTo(typedOther.isSetCreator());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCreator()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.creator, typedOther.creator);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetCreate_time()).compareTo(typedOther.isSetCreate_time());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetCreate_time()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.create_time, typedOther.create_time);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetModifier()).compareTo(typedOther.isSetModifier());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetModifier()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.modifier, typedOther.modifier);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    lastComparison = Boolean.valueOf(isSetModify_time()).compareTo(typedOther.isSetModify_time());
    if (lastComparison != 0) {
      return lastComparison;
    }
    if (isSetModify_time()) {
      lastComparison = org.apache.thrift.TBaseHelper.compareTo(this.modify_time, typedOther.modify_time);
      if (lastComparison != 0) {
        return lastComparison;
      }
    }
    return 0;
  }

  public _Fields fieldForId(int fieldId) {
    return _Fields.findByThriftId(fieldId);
  }

  public void read(org.apache.thrift.protocol.TProtocol iprot) throws org.apache.thrift.TException {
    schemes.get(iprot.getScheme()).getScheme().read(iprot, this);
  }

  public void write(org.apache.thrift.protocol.TProtocol oprot) throws org.apache.thrift.TException {
    schemes.get(oprot.getScheme()).getScheme().write(oprot, this);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder("AmoySharePoolInfo(");
    boolean first = true;

    sb.append("id:");
    sb.append(this.id);
    first = false;
    if (!first) sb.append(", ");
    sb.append("stock_today:");
    if (this.stock_today == null) {
      sb.append("null");
    } else {
      sb.append(this.stock_today);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("stock_notice:");
    if (this.stock_notice == null) {
      sb.append("null");
    } else {
      sb.append(this.stock_notice);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("creator:");
    sb.append(this.creator);
    first = false;
    if (!first) sb.append(", ");
    sb.append("create_time:");
    if (this.create_time == null) {
      sb.append("null");
    } else {
      sb.append(this.create_time);
    }
    first = false;
    if (!first) sb.append(", ");
    sb.append("modifier:");
    sb.append(this.modifier);
    first = false;
    if (!first) sb.append(", ");
    sb.append("modify_time:");
    if (this.modify_time == null) {
      sb.append("null");
    } else {
      sb.append(this.modify_time);
    }
    first = false;
    sb.append(")");
    return sb.toString();
  }

  public void validate() throws org.apache.thrift.TException {
    // check for required fields
    // check for sub-struct validity
  }

  private void writeObject(java.io.ObjectOutputStream out) throws java.io.IOException {
    try {
      write(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(out)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private void readObject(java.io.ObjectInputStream in) throws java.io.IOException, ClassNotFoundException {
    try {
      // it doesn't seem like you should have to do this, but java serialization is wacky, and doesn't call the default constructor.
      __isset_bitfield = 0;
      read(new org.apache.thrift.protocol.TCompactProtocol(new org.apache.thrift.transport.TIOStreamTransport(in)));
    } catch (org.apache.thrift.TException te) {
      throw new java.io.IOException(te);
    }
  }

  private static class AmoySharePoolInfoStandardSchemeFactory implements SchemeFactory {
    public AmoySharePoolInfoStandardScheme getScheme() {
      return new AmoySharePoolInfoStandardScheme();
    }
  }

  private static class AmoySharePoolInfoStandardScheme extends StandardScheme<AmoySharePoolInfo> {

    public void read(org.apache.thrift.protocol.TProtocol iprot, AmoySharePoolInfo struct) throws org.apache.thrift.TException {
      org.apache.thrift.protocol.TField schemeField;
      iprot.readStructBegin();
      while (true)
      {
        schemeField = iprot.readFieldBegin();
        if (schemeField.type == org.apache.thrift.protocol.TType.STOP) { 
          break;
        }
        switch (schemeField.id) {
          case 1: // ID
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.id = iprot.readI32();
              struct.setIdIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 2: // STOCK_TODAY
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list8 = iprot.readListBegin();
                struct.stock_today = new ArrayList<StockInfo>(_list8.size);
                for (int _i9 = 0; _i9 < _list8.size; ++_i9)
                {
                  StockInfo _elem10; // required
                  _elem10 = new StockInfo();
                  _elem10.read(iprot);
                  struct.stock_today.add(_elem10);
                }
                iprot.readListEnd();
              }
              struct.setStock_todayIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 3: // STOCK_NOTICE
            if (schemeField.type == org.apache.thrift.protocol.TType.LIST) {
              {
                org.apache.thrift.protocol.TList _list11 = iprot.readListBegin();
                struct.stock_notice = new ArrayList<StockInfo>(_list11.size);
                for (int _i12 = 0; _i12 < _list11.size; ++_i12)
                {
                  StockInfo _elem13; // required
                  _elem13 = new StockInfo();
                  _elem13.read(iprot);
                  struct.stock_notice.add(_elem13);
                }
                iprot.readListEnd();
              }
              struct.setStock_noticeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 4: // CREATOR
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.creator = iprot.readI32();
              struct.setCreatorIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 5: // CREATE_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.create_time = iprot.readString();
              struct.setCreate_timeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 6: // MODIFIER
            if (schemeField.type == org.apache.thrift.protocol.TType.I32) {
              struct.modifier = iprot.readI32();
              struct.setModifierIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          case 7: // MODIFY_TIME
            if (schemeField.type == org.apache.thrift.protocol.TType.STRING) {
              struct.modify_time = iprot.readString();
              struct.setModify_timeIsSet(true);
            } else { 
              org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
            }
            break;
          default:
            org.apache.thrift.protocol.TProtocolUtil.skip(iprot, schemeField.type);
        }
        iprot.readFieldEnd();
      }
      iprot.readStructEnd();

      // check for required fields of primitive type, which can't be checked in the validate method
      struct.validate();
    }

    public void write(org.apache.thrift.protocol.TProtocol oprot, AmoySharePoolInfo struct) throws org.apache.thrift.TException {
      struct.validate();

      oprot.writeStructBegin(STRUCT_DESC);
      oprot.writeFieldBegin(ID_FIELD_DESC);
      oprot.writeI32(struct.id);
      oprot.writeFieldEnd();
      if (struct.stock_today != null) {
        oprot.writeFieldBegin(STOCK_TODAY_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.stock_today.size()));
          for (StockInfo _iter14 : struct.stock_today)
          {
            _iter14.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      if (struct.stock_notice != null) {
        oprot.writeFieldBegin(STOCK_NOTICE_FIELD_DESC);
        {
          oprot.writeListBegin(new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, struct.stock_notice.size()));
          for (StockInfo _iter15 : struct.stock_notice)
          {
            _iter15.write(oprot);
          }
          oprot.writeListEnd();
        }
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(CREATOR_FIELD_DESC);
      oprot.writeI32(struct.creator);
      oprot.writeFieldEnd();
      if (struct.create_time != null) {
        oprot.writeFieldBegin(CREATE_TIME_FIELD_DESC);
        oprot.writeString(struct.create_time);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldBegin(MODIFIER_FIELD_DESC);
      oprot.writeI32(struct.modifier);
      oprot.writeFieldEnd();
      if (struct.modify_time != null) {
        oprot.writeFieldBegin(MODIFY_TIME_FIELD_DESC);
        oprot.writeString(struct.modify_time);
        oprot.writeFieldEnd();
      }
      oprot.writeFieldStop();
      oprot.writeStructEnd();
    }

  }

  private static class AmoySharePoolInfoTupleSchemeFactory implements SchemeFactory {
    public AmoySharePoolInfoTupleScheme getScheme() {
      return new AmoySharePoolInfoTupleScheme();
    }
  }

  private static class AmoySharePoolInfoTupleScheme extends TupleScheme<AmoySharePoolInfo> {

    @Override
    public void write(org.apache.thrift.protocol.TProtocol prot, AmoySharePoolInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol oprot = (TTupleProtocol) prot;
      BitSet optionals = new BitSet();
      if (struct.isSetId()) {
        optionals.set(0);
      }
      if (struct.isSetStock_today()) {
        optionals.set(1);
      }
      if (struct.isSetStock_notice()) {
        optionals.set(2);
      }
      if (struct.isSetCreator()) {
        optionals.set(3);
      }
      if (struct.isSetCreate_time()) {
        optionals.set(4);
      }
      if (struct.isSetModifier()) {
        optionals.set(5);
      }
      if (struct.isSetModify_time()) {
        optionals.set(6);
      }
      oprot.writeBitSet(optionals, 7);
      if (struct.isSetId()) {
        oprot.writeI32(struct.id);
      }
      if (struct.isSetStock_today()) {
        {
          oprot.writeI32(struct.stock_today.size());
          for (StockInfo _iter16 : struct.stock_today)
          {
            _iter16.write(oprot);
          }
        }
      }
      if (struct.isSetStock_notice()) {
        {
          oprot.writeI32(struct.stock_notice.size());
          for (StockInfo _iter17 : struct.stock_notice)
          {
            _iter17.write(oprot);
          }
        }
      }
      if (struct.isSetCreator()) {
        oprot.writeI32(struct.creator);
      }
      if (struct.isSetCreate_time()) {
        oprot.writeString(struct.create_time);
      }
      if (struct.isSetModifier()) {
        oprot.writeI32(struct.modifier);
      }
      if (struct.isSetModify_time()) {
        oprot.writeString(struct.modify_time);
      }
    }

    @Override
    public void read(org.apache.thrift.protocol.TProtocol prot, AmoySharePoolInfo struct) throws org.apache.thrift.TException {
      TTupleProtocol iprot = (TTupleProtocol) prot;
      BitSet incoming = iprot.readBitSet(7);
      if (incoming.get(0)) {
        struct.id = iprot.readI32();
        struct.setIdIsSet(true);
      }
      if (incoming.get(1)) {
        {
          org.apache.thrift.protocol.TList _list18 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.stock_today = new ArrayList<StockInfo>(_list18.size);
          for (int _i19 = 0; _i19 < _list18.size; ++_i19)
          {
            StockInfo _elem20; // required
            _elem20 = new StockInfo();
            _elem20.read(iprot);
            struct.stock_today.add(_elem20);
          }
        }
        struct.setStock_todayIsSet(true);
      }
      if (incoming.get(2)) {
        {
          org.apache.thrift.protocol.TList _list21 = new org.apache.thrift.protocol.TList(org.apache.thrift.protocol.TType.STRUCT, iprot.readI32());
          struct.stock_notice = new ArrayList<StockInfo>(_list21.size);
          for (int _i22 = 0; _i22 < _list21.size; ++_i22)
          {
            StockInfo _elem23; // required
            _elem23 = new StockInfo();
            _elem23.read(iprot);
            struct.stock_notice.add(_elem23);
          }
        }
        struct.setStock_noticeIsSet(true);
      }
      if (incoming.get(3)) {
        struct.creator = iprot.readI32();
        struct.setCreatorIsSet(true);
      }
      if (incoming.get(4)) {
        struct.create_time = iprot.readString();
        struct.setCreate_timeIsSet(true);
      }
      if (incoming.get(5)) {
        struct.modifier = iprot.readI32();
        struct.setModifierIsSet(true);
      }
      if (incoming.get(6)) {
        struct.modify_time = iprot.readString();
        struct.setModify_timeIsSet(true);
      }
    }
  }

}

