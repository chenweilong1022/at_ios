<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList(1)">
      <el-form-item>
        <el-input v-model="dataForm.nickName" placeholder="昵称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select v-model="dataForm.nation" placeholder="拉群号国家" clearable>
          <el-option
            v-for="item in countryCodes"
            :key="item.value"
            :label="item.value"
            :value="item.value">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-input v-model="dataForm.telephone" placeholder="手机号" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="dataForm.registerCount" placeholder="卡注册次数" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select
          v-model="dataForm.userGroupId"
          filterable clearable
          remote
          placeholder="选择分组"
          :remote-method="queryUserGroupBySearchWord">
          <el-option
            v-for="item in userGroupOptions"
            :key="item.id"
            :label="item.name"
            :value="item.id">
          </el-option>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select
          v-model="dataForm.status"
          class="m-2" clearable
          placeholder="账户状态"
          style="width: 240px">
          <el-option
            v-for="item in statusOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-select
          v-model="dataForm.customerServiceId"
          filterable clearable
          remote
          placeholder="选择客服"
          :remote-method="queryCustomerByFuzzyName">
          <el-option
            v-for="item in customerUserOptions"
            :key="item.userId"
            :label="item.nickname"
            :value="item.userId">
          </el-option>
        </el-select>
        <el-form-item>
          <el-select
            v-model="dataForm.validateFlag"
            class="m-2" clearable
            placeholder="是否有验活记录"
            style="width: 240px">
            <el-option
              v-for="item in validateOptions"
              :key="item.value"
              :label="item.label"
              :value="item.value"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="dataForm.userSource" placeholder="账户来源" clearable>
            <el-option
              v-for="item in atUserSourceCodes"
              :key="item.key"
              :label="item.value"
              :value="item.key">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-select v-model="dataForm.tokenOpenStatus" placeholder="打开状态" clearable>
            <el-option
              v-for="item in openStatusCodes"
              :key="item.key"
              :label="item.value"
              :value="item.key">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-input v-model="dataForm.selectLimit" placeholder="查询条数" clearable></el-input>
        </el-form-item>
        <el-button @click="getDataList(1)">查询</el-button>
        <div>
        <el-button v-if="isAuth('ltt:atuser:save')" type="primary"
                   @click="userImportHandle()">账户导入
        </el-button>
        <el-button v-if="isAuth('ltt:atuser:save')" type="primary"
                   @click="userTokenOrderHandle()">账户购买
        </el-button>
        <el-button v-if="isAuth('ltt:atuser:save')" type="primary" @click="userTransferGroupHandle()"
                   :disabled="dataListSelections.length <= 0">转移分组
        </el-button>
        <el-button v-if="isAuth('ltt:atuser:save')" type="primary"
                   @click="importTokenHandle()"
                   :disabled="dataListSelections.length <= 0">token导出
        </el-button>
        <el-button v-if="isAuth('ltt:atuser:save')" type="primary"
                   @click="userCustomerHandle()"
                   :disabled="dataListSelections.length <= 0">分配客服
        </el-button>
        <el-button style="margin-top: 15px;" v-if="isAuth('ltt:atuser:save')" type="primary"
                   @click="userValidateHandle()">验活账号
        </el-button>
        <el-button style="margin-top: 15px;" v-if="isAuth('ltt:atuser:save')" type="primary"
                   @click="maintainUserHandle()":disabled="dataListSelections.length <= 0">养号
        </el-button>
        <el-button style="margin-top: 15px;" v-if="isAuth('ltt:atuser:delete')" type="danger" @click="deleteHandle()"
                   :disabled="dataListSelections.length <= 0">批量删除
        </el-button>
        <el-button style="margin-top: 15px;" v-if="isAuth('ltt:atuser:save')" type="danger"
                   @click="cleanBlockData()">清理封号
        </el-button>
        <el-button type="primary" @click="copyPhoneHandle()"
                   :disabled="dataListSelections.length <= 0">复制手机号
        </el-button>
        </div>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      @sort-change="onSortChange"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%;">
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50">
      </el-table-column>
      <el-table-column
        prop="id"
        header-align="center"
        align="center"
        label="id">
      </el-table-column>
      <el-table-column
        prop="nation"
        header-align="center"
        align="center"
        label="国家">
      </el-table-column>
      <el-table-column
        prop="telephone"
        header-align="center"
        align="center" width="150px"
        label="电话">
        <template slot-scope="scope">
          <el-badge :value="scope.row.registerCount" :class="badgeClass(scope.row.phoneState)" class="item">
            <el-button size="text" @click="copyPhoneHandle(scope.row.telephone)">{{ scope.row.telephone }}</el-button>
          </el-badge>
        </template>
      </el-table-column>
      <el-table-column
        prop="nickName"
        header-align="center"
        align="center"
        label="昵称">
      </el-table-column>
      <el-table-column
        prop="userGroupName"
        header-align="center"
        align="center"
        label="分组名称">
      </el-table-column>
      <el-table-column
        prop="userSource"
        header-align="center"
        align="center"
        label="账号来源">
        <template slot-scope="scope">
          <el-tag v-for="item in atUserSourceCodes" :key="item.key" v-if="scope.row.userSource === item.key">
            {{ item.value }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="status"
        header-align="center"
        align="center"
        label="状态">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.status === 1" size="small" type="danger">未验证</el-tag>
          <el-tag v-else-if="scope.row.status === 2" size="small" type="danger">封号</el-tag>
          <el-tag v-else-if="scope.row.status === 3" size="small" type="danger">下线</el-tag>
          <el-tag v-else-if="scope.row.status === 4" size="small" type="danger">在线</el-tag>
          <el-tag v-else-if="scope.row.status === 6" size="small" type="danger">已使用</el-tag>
          <el-tag v-else size="small">数据错误</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        align="center"
        label="创建时间">
      </el-table-column>
      <el-table-column
        prop="tokenOpenStatus"
        header-align="center"
        align="center"
        label="打开状态">
        <template slot-scope="scope">
          <el-tag v-for="item in openStatusCodes" :key="item.key" v-if="scope.row.tokenOpenStatus === item.key">
            {{ item.value }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="tokenOpenTime"
        header-align="center"
        align="center"
        sortable
        label="打开时间">
      </el-table-column>
      <el-table-column
        prop="status"
        header-align="center"
        align="center"
        label="日志信息">
        <template slot-scope="scope">
          {{scope.row.msg}}/{{scope.row.tokenErrMsg}}
        </template>
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="addOrUpdateHandle(scope.row.id)">修改</el-button>
          <el-button type="text" size="small" @click="deleteHandle(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20,30, 50, 100,500, 1000]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper">
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update v-if="addOrUpdateVisible" ref="addOrUpdate" @refreshDataList="getDataList"></add-or-update>
    <user-import  v-if="userImportVisible" ref="userImport" @refreshDataList="getDataList"></user-import>
    <user-token-order  v-if="userTokenOrderVisible" ref="userTokenOrder" @refreshDataList="getDataList"></user-token-order>
    <user-transfer-group  v-if="userTransferGroupVisible" ref="userTransferGroup" @refreshDataList="getDataList"></user-transfer-group>
    <user-customer-group  v-if="userCustomerVisible" ref="userCustomerGroup" @refreshDataList="getDataList"></user-customer-group>
  </div>
</template>

<script>
  import AddOrUpdate from './atuser-add-or-update'
  import UserImport from './atusertoken-add-or-update'
  import UserTokenOrder from './atusertoken-order'
  import UserTransferGroup from './atuserTransferGroup-add-or-update'
  import UserCustomerGroup from './atuserCustomer-add-or-update'
  export default {
    data () {
      return {
        userGroupOptions: [],
        customerUserOptions: [],
        countryCodes: [],
        statusOptions: [
          {
            value: 1,
            label: '未验证'
          },
          {
            value: 2,
            label: '封号'
          },
          {
            value: 3,
            label: '下线'
          },
          {
            value: 4,
            label: '在线'
          },
          {
            value: 5,
            label: '数据错误'
          },
          {
            value: 6,
            label: '已使用'
          },
          {
            value: 7,
            label: '需要刷新token'
          }
        ],
        validateOptions: [
          {
            value: 0,
            label: '否'
          },
          {
            value: 1,
            label: '是'
          }
        ],
        dataForm: {
          userId: null,
          nickName: '',
          nation: '',
          telephone: '',
          userGroupId: null,
          status: null,
          customerServiceId: null,
          validateFlag: null,
          userSource: null,
          selectLimit: null,
          tokenOpenStatus: null,
          tokenOpenTimeSort: null,
          registerCount: null,
          tokenOpenTime: null,
          tokenErrMsg: null
        },
        customerServiceField: '',
        dataList: [],
        atUserSourceCodes: [],
        openStatusCodes: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        userImportVisible: false,
        userTokenOrderVisible: false,
        userTransferGroupVisible: false,
        userCustomerVisible: false,
        redPhoneCount: 0
      }
    },
    components: {
      UserImport,
      UserTokenOrder,
      UserTransferGroup,
      UserCustomerGroup,
      AddOrUpdate
    },
    activated () {
      this.init()
      this.getDataList()
      this.getAtUserSource()
      this.getOpenStatus()
      this.getCountryCodeEnums()
      this.queryCustomerByFuzzyName('')
      this.queryUserGroupBySearchWord('')
    },
    methods: {
      init() {
        this.dataForm.userId = this.$route.query.userId
      },
      onSortChange ({ column, prop, order }) {
        // column 是当前列的引用
        // prop 是排序的列的属性名
        // order 是排序的顺序，'ascending' 或 'descending'
        console.log('Sort by', prop, 'with order', order)
        // 根据 prop 和 order 对数据进行排序
        // 排序 0正序 1倒序
        if (order == null) {
          this.dataForm.tokenOpenTimeSort = null
        } else {
          this.dataForm.tokenOpenTimeSort = order === 'ascending' ? 0 : 1
        }
        this.getDataList()
      },
      getCountryCodeEnums () {
        this.$http({
          url: this.$http.adornUrl(`/app/enums/countryCodes`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.countryCodes = data.data
          } else {
            this.$message.error(data.msg)
          }
        })
      },
      // 获取数据列表
      getDataList (currentPage) {
        this.dataListLoading = true
        if (this.dataForm.selectLimit != null && this.dataForm.selectLimit > 0) {
          this.pageIndex = 1
          this.pageSize = this.dataForm.selectLimit
        }
        if (currentPage != null && currentPage > 0) {
          this.pageIndex = 1
        }
        this.$http({
          url: this.$http.adornUrl('/ltt/atuser/list'),
          method: 'get',
          params: this.$http.adornParams({
            'page': this.pageIndex,
            'limit': this.pageSize,
            'nickName': this.dataForm.nickName,
            'nation': this.dataForm.nation,
            'telephone': this.dataForm.telephone,
            'userGroupId': this.dataForm.userGroupId,
            'status': this.dataForm.status,
            'customerServiceId': this.dataForm.customerServiceId,
            'customerService': this.dataForm.customerService,
            'validateFlag': this.dataForm.validateFlag,
            'userSource': this.dataForm.userSource,
            'id': this.dataForm.userId,
            'selectLimit': this.dataForm.selectLimit,
            'tokenOpenStatus': this.dataForm.tokenOpenStatus,
            'tokenOpenTimeSort': this.dataForm.tokenOpenTimeSort,
            'registerCount': this.dataForm.registerCount
          })
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.dataList = data.page.list
            this.totalPage = data.page.totalCount
          } else {
            this.dataList = []
            this.totalPage = 0
          }
          this.dataListLoading = false
        })
      },
      badgeClass(state) {
        if (state === true) {
          return 'green-badge'
        }
        if (state === false) {
          return 'red-badge'
        }
      },
      copyPhoneHandle (telephone) {
        var telephones = telephone ? [telephone.trim()] : this.dataListSelections.map(item => {
          return item.telephone.trim()
        })
        navigator.clipboard.writeText(telephones).then(() => {
          this.$message.success('手机号复制成功！')
        })
      },
      getBadgeType (registerCount) {
        if (registerCount === null) {
          return ''
        }
        if (registerCount === 1) {
          return 'success'
        } else if (registerCount === 2) {
          return 'warning'
        } else if (registerCount >= 3) {
          return 'danger'
        }
        return ''
      },
      // 每页数
      sizeChangeHandle (val) {
        this.pageSize = val
        this.pageIndex = 1
        this.getDataList()
      },
      // 当前页
      currentChangeHandle (val) {
        this.pageIndex = val
        this.getDataList()
      },
      // 多选
      selectionChangeHandle (val) {
        this.dataListSelections = val
      },
      // 新增 / 修改
      addOrUpdateHandle (id) {
        this.addOrUpdateVisible = true
        this.$nextTick(() => {
          this.$refs.addOrUpdate.init(id)
        })
      },
      // 新增 / 修改
      userImportHandle (id) {
        this.userImportVisible = true
        this.$nextTick(() => {
          this.$refs.userImport.init(id)
        })
      },
      // 账户购买
      userTokenOrderHandle (id) {
        this.userTokenOrderVisible = true
        this.$nextTick(() => {
          this.$refs.userTokenOrder.init(id)
        })
      },
      // 转移分组
      userTransferGroupHandle(id) {
        this.userTransferGroupVisible = true
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.phoneRed()
        var greenPhoneCount = ids.length - this.redPhoneCount
        this.$nextTick(() => {
          this.$refs.userTransferGroup.init(ids, this.redPhoneCount, greenPhoneCount)
        })
      },
      // 红灯数量
      phoneRed () {
        var redPhoneCount = this.dataListSelections
          .filter(item => {
            return item.phoneState === false
          }).map(item => {
            return item.phoneState
          })
        this.redPhoneCount = redPhoneCount.length
      },
      // 分配客服
      userCustomerHandle (id) {
        this.userCustomerVisible = true
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对所选项进行客服分配操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$nextTick(() => {
            this.$refs.userCustomerGroup.init(ids)
          })
        })
      },
      // 验活账号
      userValidateHandle () {
        var ids = this.dataListSelections.map(item => {return item.id})
        // 是否验活全部 true：全部
        var validateFlag = ids.length > 0 ? 0 : 1
        this.$confirm(`确定要对[${validateFlag ? '所有' : '勾选'}]账户进行验活操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/ltt/atuser/validateUserStatus'),
            method: 'post',
            data: this.$http.adornData({
              'ids': ids,
              'validateFlag': validateFlag
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功，正在进行验活操作，请稍后',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        })
      },
      // 养号
      maintainUserHandle () {
        var ids = this.dataListSelections.map(item => {return item.id})
        this.$confirm(`确定要对勾选账户进行 养号 操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/ltt/atuser/maintainUser'),
            method: 'post',
            data: this.$http.adornData({
              'ids': ids,
              'validateFlag': false
            })
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功，正在进行养号操作，请稍后',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        })
      },
      // 下载账户txt
      importTokenHandle () {
        var ids = this.dataListSelections.map(item => {return item.id})
        this.$confirm(`确定要对勾选账户token导出?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          window.open(this.$http.adornUrl(`/ltt/atuser/importToken?token=${this.$cookie.get('token')}&ids=${ids}`))

          // this.$http({
          //   url: this.$http.adornUrl('/ltt/atuser/importToken'),
          //   method: 'post',
          //   data: this.$http.adornData(ids, false)
          // }).then(({data}) => {
          //   console.log("----------------")
          //   if (data && data.code === 0) {
          //     /**
          //      * 下载的文件的名字
          //      */
          //     var fileName = this.getFilename(data.fileUrl)
          //     console.log(data.fileUrl)
          //     var link = document.createElement('a')
          //     // 这里是将url转成blob地址，
          //     fetch(data.fileUrl).then((res) => res.blob())
          //       .then((blob) => {
          //         // 将链接地址字符内容转变成blob地址
          //         link.href = URL.createObjectURL(blob)
          //         console.log(link.href)
          //         /**
          //          * 下载的文件的名字
          //          */
          //         link.download = fileName
          //         document.body.appendChild(link)
          //         link.click()
          //       })
          //   } else {
          //     this.$message.error(data.msg)
          //   }
          // })
        })
      },
      getFilename(url) {
        // 从图片链接中提取文件名
        return url.substring(url.lastIndexOf('/')+1);
      },
      // 删除
      deleteHandle (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对[id=${ids.join(',')}]进行[${id ? '删除' : '批量删除'}]操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/ltt/atuser/delete'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        })
      },
      // 清理封号账号
      cleanBlockData (id) {
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对封号数据进行清理?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/ltt/atuser/cleanBlockData'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              this.$message({
                message: '操作成功',
                type: 'success',
                duration: 1500,
                onClose: () => {
                  this.getDataList()
                }
              })
            } else {
              this.$message.error(data.msg)
            }
          })
        })
      },
      /*
        根据搜索词，查询用户分组
     */
      queryUserGroupBySearchWord (serchKey) {
        serchKey = serchKey == null ? '' : serchKey + ''
        this.$http({
          url: this.$http.adornUrl(`/ltt/atusergroup/queryByFuzzyName?searchWord=${serchKey}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.userGroupOptions = [{id: 0, name: '未分组'},...data.groupList]
          }
        })
      },
      /*
        根据搜索词，查询客服
     */
      queryCustomerByFuzzyName (serchKey) {
        serchKey = serchKey == null ? '' : serchKey + ''
        this.$http({
          url: this.$http.adornUrl(`/ltt/customeruser/queryCustomerByFuzzyName?key=${serchKey}`),
          method: 'get',
          params: this.$http.adornParams()
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.customerUserOptions = [{userId: 0, nickname: '未分配'},...data.customerList]
          }
        })
      },
      getAtUserSource () {
        this.$http({
          url: this.$http.adornUrl(`/app/enums/getAtUserSource`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.atUserSourceCodes = data.data
          } else {
            this.$message.error(data.msg)
          }
        })
      },
      getOpenStatus () {
        this.$http({
          url: this.$http.adornUrl(`/app/enums/getOpenStatus`),
          method: 'get'
        }).then(({data}) => {
          if (data && data.code === 0) {
            this.openStatusCodes = data.data
          } else {
            this.$message.error(data.msg)
          }
        })
      }
    }
  }
</script>
<style>
.item {
  margin-top: 10px;
}
/* 定义绿色背景样式 */
.green-badge .el-badge__content {
  background-color: green;
  color: white;
}

/* 定义红色背景样式 */
.red-badge .el-badge__content {
  background-color: red;
  color: white;
}

</style>
