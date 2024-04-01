<template>
  <div class="mod-config">
    <el-form :inline="true" :model="dataForm" @keyup.enter.native="getDataList()">
      <el-form-item>
        <el-input v-model="dataForm.nickName" placeholder="昵称" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="dataForm.nation" placeholder="国家" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-input v-model="dataForm.telephone" placeholder="手机号" clearable></el-input>
      </el-form-item>
      <el-form-item>
        <el-select
          v-model="dataForm.userGroupId"
          filterable clearable
          remote
          placeholder="选择分组"
          :remote-method="queryUserGroupBySearchWord"
          :loading="loading">
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
          v-model="dataForm.userSource"
          class="m-2" clearable
          placeholder="账户状态"
          style="width: 240px">
          <el-option
            v-for="item in atUserSourceCodes"
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
          :remote-method="queryCustomerByFuzzyName"
          :loading="loading">
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
          <el-input v-model="dataForm.selectLimit" placeholder="查询条数" clearable></el-input>
        </el-form-item>
        <el-button @click="getDataList()">查询</el-button>
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
                   @click="downloadUserTokenTxtHandle()"
                   :disabled="dataListSelections.length <= 0">下载账户txt
        </el-button>
        <el-button v-if="isAuth('ltt:atuser:save')" type="primary"
                   @click="userCustomerHandle()"
                   :disabled="dataListSelections.length <= 0">分配客服
        </el-button>
        <el-button style="margin-top: 15px;" v-if="isAuth('ltt:atuser:save')" type="primary"
                   @click="userValidateHandle()">验活账号
        </el-button>
        <el-button style="margin-top: 15px;" v-if="isAuth('ltt:atuser:delete')" type="danger" @click="deleteHandle()"
                   :disabled="dataListSelections.length <= 0">批量删除
        </el-button>
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
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
        prop="avatar"
        header-align="center"
        align="center"
        label="头像">
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
        align="center"
        label="电话">
      </el-table-column>
      <el-table-column
        prop="nickName"
        header-align="center"
        align="center"
        label="昵称">
      </el-table-column>
      <el-table-column
        prop="numberFriends"
        header-align="center"
        align="center"
        label="好友数量">
      </el-table-column>
      <el-table-column
        prop="password"
        header-align="center"
        align="center"
        label="密码">
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
          <el-tag v-else size="small">数据错误</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="customerService"
        header-align="center"
        align="center"
        label="所属客服">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.customerServiceId == null" size="small" type="danger">未分配</el-tag>
          <el-tag v-else size="small">{{scope.row.customerService}}</el-tag>
        </template>
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        align="center"
        label="创建时间">
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
      :page-sizes="[10, 20, 50, 100]"
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
          nickName: '',
          nation: '',
          telephone: '',
          userGroupId: null,
          status: null,
          customerServiceId: null,
          validateFlag: null,
          userSource: null,
          selectLimit: null
        },
        customerServiceField: '',
        dataList: [],
        atUserSourceCodes: [],
        pageIndex: 1,
        pageSize: 10,
        totalPage: 0,
        dataListLoading: false,
        dataListSelections: [],
        addOrUpdateVisible: false,
        userImportVisible: false,
        userTokenOrderVisible: false,
        userTransferGroupVisible: false,
        userCustomerVisible: false
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
      this.getDataList()
      this.getAtUserSource()
    },
    methods: {
      // 获取数据列表
      getDataList () {
        this.dataListLoading = true
        if (this.dataForm.selectLimit != null && this.dataForm.selectLimit > 0) {
          this.pageIndex = 1
          this.pageSize = this.dataForm.selectLimit
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
            'selectLimit': this.dataForm.selectLimit
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
      userTransferGroupHandle (id) {
        this.userTransferGroupVisible = true
        var ids = id ? [id] : this.dataListSelections.map(item => {
          return item.id
        })
        this.$confirm(`确定对所选项进行分组转移操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$nextTick(() => {
            this.$refs.userTransferGroup.init(ids)
          })
        })
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
      // 下载账户txt
      downloadUserTokenTxtHandle () {
        var ids = this.dataListSelections.map(item => {return item.id})
        this.$confirm(`确定要对勾选账户进行账户下载操作?`, '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$http({
            url: this.$http.adornUrl('/ltt/atuser/downloadUserTokenTxt'),
            method: 'post',
            data: this.$http.adornData(ids, false)
          }).then(({data}) => {
            if (data && data.code === 0) {
              /**
               * 下载的文件的名字
               */
              var fileName = this.getFilename(data.fileUrl)
              console.log(data.fileUrl)
              var link = document.createElement('a')
              // 这里是将url转成blob地址，
              fetch(data.fileUrl).then((res) => res.blob())
                .then((blob) => {
                  // 将链接地址字符内容转变成blob地址
                  link.href = URL.createObjectURL(blob)
                  console.log(link.href)
                  /**
                   * 下载的文件的名字
                   */
                  link.download = fileName
                  document.body.appendChild(link)
                  link.click()
                })
            } else {
              this.$message.error(data.msg)
            }
          })
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
            this.userGroupOptions = data.groupList
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
      }
    }
  }
</script>
<style>
</style>
