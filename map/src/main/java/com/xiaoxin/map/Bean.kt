package com.xiaoxin.map

import com.xiaoxin.network.retrofit.bean.BaseHttpResponse

class MapHttpResponse<T> :BaseHttpResponse<T>(){

}

object GoogleBean {

    data class GeoCodes(

        var error_message:String,
        var results: List<Result>,
        /**
         * "OK"表示没有发生错误；地址已成功解析，并且至少返回了一个地理编码。
         * "ZERO_RESULTS"表示地理编码成功但未返回任何结果。如果地理编码器传递了一个不存在的address.
         * "OVER_QUERY_LIMIT"表示您已超出配额。
         * "REQUEST_DENIED"表示您的请求被拒绝。该网页不允许使用地理编码器。
         * "INVALID_REQUEST"通常表示查询 ( address, components或latlng) 缺失。
         * "UNKNOWN_ERROR"表示由于服务器错误而无法处理请求。如果您再试一次，请求可能会成功。
         * "ERROR"表示请求超时或联系 Google 服务器时出现问题。如果您再试一次，请求可能会成功。
         */
        var status: String
    )

    data class Result(
        //一个包含适用于该地址的单独组件的数组
        var address_components: List<AddressComponent>,
        //是一个包含此位置的人类可读地址的字符串 + 邮编
        var formatted_address: String,

        var geometry: Geometry,
        //地点的唯一标识符
        var place_id: String,
        /**
         * street_address表示准确的街道地址。
         * route表示命名路线（例如“US 101”）。
         * intersection表示主要路口，通常是两条主要道路。
         * political表示一个政治实体。通常，这种类型表示某些民政部门的多边形。
         * country表示国家政治实体，通常是地理编码器返回的最高阶类型。
         * administrative_area_level_1表示国家级以下的一级民事实体。在美国，这些行政级别是州。并非所有国家都表现出这些行政级别。在大多数情况下，administrative_area_level_1 短名称将与 ISO 3166-2 细分和其他广泛传播的列表紧密匹配；然而，这并不能保证，因为我们的地理编码结果基于各种信号和位置数据。
         * administrative_area_level_2表示国家级以下的二级民事实体。在美国，这些行政级别是县。并非所有国家都表现出这些行政级别。
         * administrative_area_level_3表示国家级以下的三级民事实体。这种类型表示轻微的民事部门。并非所有国家都表现出这些行政级别。
         * administrative_area_level_4表示国家级以下的四阶民事实体。这种类型表示轻微的民事部门。并非所有国家都表现出这些行政级别。
         * administrative_area_level_5表示国家级以下的五阶民事实体。这种类型表示轻微的民事部门。并非所有国家都表现出这些行政级别。
         * administrative_area_level_6表示国家级以下的六阶民事实体。这种类型表示轻微的民事部门。并非所有国家都表现出这些行政级别。
         * administrative_area_level_7表示国家级以下的七级民事实体。这种类型表示轻微的民事部门。并非所有国家都表现出这些行政级别。
         * colloquial_area指示实体的常用替代名称。
         * locality表示合并的城市或城镇政治实体。
         * sublocality表示地方以下的一级民事实体。对于某些位置，可能会收到其中一种附加类型： sublocality_level_1to sublocality_level_5。每个地方级别都是一个民事实体。较大的数字表示较小的地理区域。
         * neighborhood表示一个命名的邻域
         * premise表示一个命名的位置，通常是具有共同名称的建筑物或建筑物集合
         * subpremise表示指定位置下方的一阶实体，通常是具有共同名称的建筑物集合中的单个建筑物
         * plus_code表示从纬度和经度派生的编码位置参考。Plus 代码可用于在不存在街道地址的地方（建筑物未编号或街道未命名）替代街道地址。有关详细信息，请参阅https://plus.codes 。
         * postal_code表示用于处理国内邮政邮件的邮政编码。
         * natural_feature表示突出的自然特征。
         * airport表示机场。
         * park表示一个命名的公园。
         * point_of_interest表示指定的兴趣点。通常，这些“POI”是不容易归入其他类别的著名本地实体，例如“帝国大厦”或“埃菲尔铁塔”。
         * floor表示建筑物地址的楼层。
         * establishment通常表示尚未分类的地点。
         * landmark指示用作参考的附近地点，以帮助导航。
         * point_of_interest表示指定的兴趣点。
         * parking表示停车场或停车场。
         * post_box表示特定的邮箱。
         * postal_town表示一组地理区域，例如 locality和sublocality，用于某些国家/地区的邮寄地址。
         * room表示建筑物地址的房间。
         * street_number表示准确的街道编号。
         * bus_station，train_station并 transit_station指明公共汽车、火车或公共交通站的位置。
         * */
        var types: List<String>


    )

    data class AddressComponent(
        //地理编码器返回的地址组件的全文描述或名称
        var long_name: String,
        //是地址组件的缩写文本名称（如果可用）
        var short_name: String,
        //数组，指示地址组件的类型
        var types: List<String>
    )

    data class Geometry(
        //存储 LatLngBounds可以完全包含返回结果的。请注意，这些边界可能与推荐的视口不匹配。
        var bounds: Bounds,
        //包含地理编码的纬度、经度值。请注意，我们将此位置作为LatLng对象返回，而不是作为格式化字符串。
        var location: Location,
        //ROOFTOP表示返回的结果反映了精确的地理编码。
        //RANGE_INTERPOLATED 表示返回的结果反映了在两个精确点（例如交叉点）之间插值的近似值（通常在道路上）。当街道地址的屋顶地理编码不可用时，通常会返回插值结果。
        //GEOMETRIC_CENTER 表示返回的结果是折线（例如街道）或多边形（区域）等结果的几何中心。
        //APPROXIMATE 表示返回的结果是近似的。
        var location_type: String,
        //存储返回结果的推荐视口。
        var viewport: Viewport
    )

    data class Bounds(
        var northeast: Northeast,
        var southwest: Southwest
    )

    data class Location(
        var lat: Double,
        var lng: Double
    )

    data class Viewport(
        var northeast: NortheastX,
        var southwest: SouthwestX
    )

    data class Northeast(
        var lat: Double,
        var lng: Double
    )

    data class Southwest(
        var lat: Double,
        var lng: Double
    )

    data class NortheastX(
        var lat: Double,
        var lng: Double
    )

    data class SouthwestX(
        var lat: Double,
        var lng: Double
    )

}

